package game;

import model.Board;
import java.util.Scanner;

public class GameEngine {
    private Board board;

    public void startGame() {
        board = new Board(10);  // Create a 10x10 grid
        board.initializeBoard();
        board.startTimer();

        Scanner scanner = new Scanner(System.in);
        System.out.println("🎮 Welcome to Word Search Game!");

        while (!board.isGameOver()) {
            if (board.isTimeUp()) {
                System.out.println("⏰ Time's up! Game Over.");
                break;
            }

            board.displayBoard();
            System.out.println("⏳ Time left: " + board.getRemainingTime() + " seconds");
            System.out.println("Enter: word x1 y1 x2 y2 OR 'undo' / 'redo' / 'history'");

            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("undo")) {
                if (board.undoLastMove()) System.out.println("✔️ Undo successful.");
                else System.out.println("❌ Nothing to undo.");
            } else if (input.equalsIgnoreCase("redo")) {
                if (board.redoLastMove()) System.out.println("✔️ Redo successful.");
                else System.out.println("❌ Nothing to redo.");
            } else if (input.equalsIgnoreCase("history")) {
                board.printHistory();
            } else {
                String[] parts = input.split("\\s+");
                if (parts.length != 5) {
                    System.out.println("❌ Invalid input. Format: word x1 y1 x2 y2");
                    continue;
                }

                try {
                    String word = parts[0];
                    int x1 = Integer.parseInt(parts[1]);
                    int y1 = Integer.parseInt(parts[2]);
                    int x2 = Integer.parseInt(parts[3]);
                    int y2 = Integer.parseInt(parts[4]);

                    if (board.validateWord(word, x1, y1, x2, y2)) {
                        System.out.println("✅ Correct!");
                    } else {
                        System.out.println("❌ Incorrect guess or already found.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("❌ Coordinates must be integers.");
                }
            }
        }

        if (board.isGameOver()) {
            System.out.println("🎉 Congratulations! You found all the words.");
        }

        scanner.close();
    }
}
