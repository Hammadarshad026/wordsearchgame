package model;

import java.util.*;

public class Board {
    private final char[][] grid;
    private final Set<String> wordList;
    private final Set<String> foundWords;
    private final List<String> history = new ArrayList<>();
    private final Deque<String> undoStack = new ArrayDeque<>();
    private final Deque<String> redoStack = new ArrayDeque<>();
    private final int size;
    private long startTime;
    private final int timeLimitSeconds = 120; // 2 minutes

    public Board(int size) {
        this.size = size;
        this.grid = new char[size][size];
        this.wordList = new HashSet<>();
        this.foundWords = new HashSet<>();
    }

    public void initializeBoard() {
        fillWithPlaceholders();
        populateWords();
        fillRemainingWithRandomLetters();
    }

    public void startTimer() {
        this.startTime = System.currentTimeMillis();
    }

    public boolean isTimeUp() {
        long now = System.currentTimeMillis();
        return (now - startTime) / 1000 >= timeLimitSeconds;
    }

    public long getRemainingTime() {
        long now = System.currentTimeMillis();
        return Math.max(0, timeLimitSeconds - (now - startTime) / 1000);
    }

    public boolean validateWord(String word, int x1, int y1, int x2, int y2) {
        word = WordUtils.format(word);
        if (foundWords.contains(word)) return false;

        String extracted = extractWord(x1, y1, x2, y2);
        if (wordList.contains(word) && word.equals(extracted)) {
            foundWords.add(word);
            history.add(word);
            undoStack.push(word);
            redoStack.clear();
            return true;
        }
        return false;
    }

    public boolean undoLastMove() {
        if (!undoStack.isEmpty()) {
            String last = undoStack.pop();
            foundWords.remove(last);
            redoStack.push(last);
            history.remove(history.size() - 1);
            return true;
        }
        return false;
    }

    public boolean redoLastMove() {
        if (!redoStack.isEmpty()) {
            String word = redoStack.pop();
            foundWords.add(word);
            history.add(word);
            undoStack.push(word);
            return true;
        }
        return false;
    }

    public void printHistory() {
        System.out.println("📜 Game History:");
        for (String word : history) {
            System.out.println("- " + word);
        }
    }

    public boolean isGameOver() {
        return foundWords.containsAll(wordList);
    }

    private void fillWithPlaceholders() {
        for (int i = 0; i < size; i++) {
            Arrays.fill(grid[i], '-');
        }
    }

    private void populateWords() {
        String[] words = {"JAVA", "PAK", "SCOT"};
        Collections.addAll(wordList, words);

        Random random = new Random();
        for (String word : words) {
            boolean isPlaced = false;
            int tries = 0;

            while (!isPlaced && tries < 100) {
                int row = random.nextInt(size);
                int colStart = random.nextInt(size - word.length());

                if (canPlaceHorizontally(row, colStart, word)) {
                    for (int i = 0; i < word.length(); i++) {
                        grid[row][colStart + i] = word.charAt(i);
                    }
                    isPlaced = true;
                }

                tries++;
            }

            if (!isPlaced) {
                System.out.println("⚠️ Could not place word: " + word);
            }
        }
    }

    private boolean canPlaceHorizontally(int row, int colStart, String word) {
        for (int i = 0; i < word.length(); i++) {
            char c = grid[row][colStart + i];
            if (c != '-' && c != word.charAt(i)) return false;
        }
        return true;
    }

    private void fillRemainingWithRandomLetters() {
        Random rng = new Random();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (grid[i][j] == '-') {
                    grid[i][j] = (char) ('A' + rng.nextInt(26));
                }
            }
        }
    }

    public void displayBoard() {
        System.out.println("\nGame Board:");
        for (char[] row : grid) {
            for (char c : row) {
                System.out.print(c + " ");
            }
            System.out.println();
        }
    }

    private String extractWord(int x1, int y1, int x2, int y2) {
        StringBuilder sb = new StringBuilder();
        int dx = Integer.compare(x2, x1);
        int dy = Integer.compare(y2, y1);
        int len = Math.max(Math.abs(x2 - x1), Math.abs(y2 - y1)) + 1;

        for (int i = 0; i < len; i++) {
            int row = x1 + i * dx;
            int col = y1 + i * dy;
            if (row >= 0 && row < size && col >= 0 && col < size) {
                sb.append(grid[row][col]);
            }
        }

        return sb.toString();
    }
}
