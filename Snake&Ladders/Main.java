import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter board dimension n (board will be n x n): ");
        int n = scanner.nextInt();

        System.out.print("Enter number of players: ");
        int playerCount = scanner.nextInt();

        System.out.print("Enter difficulty level (easy/hard): ");
        String difficulty = scanner.next().trim().toLowerCase();

        if (n <= 1) {
            System.out.println("Board dimension must be greater than 1.");
            return;
        }

        if (playerCount < 2) {
            System.out.println("At least 2 players are required.");
            return;
        }

        if (!difficulty.equals("easy") && !difficulty.equals("hard")) {
            System.out.println("Difficulty must be either easy or hard.");
            return;
        }

        BoardFactory boardFactory = new BoardFactory();
        Board board = boardFactory.createRandomBoard(n);

        List<Player> players = new ArrayList<>();
        for (int i = 1; i <= playerCount; i++) {
            players.add(new Player("P" + i));
        }

        Dice dice = new Dice();
        IMakeMoveStrategy strategy = difficulty.equals("hard") ? new Hard() : new Easy();

        GameFactory gameFactory = new GameFactory();
        Game game = gameFactory.createGame(players, board, dice, strategy);

        printGeneratedBoard(board);
        System.out.println();
        game.start();
    }

    private static void printGeneratedBoard(Board board) {
        System.out.println();
        System.out.println("Generated board details:");
        System.out.println("Board size: " + board.getDimension() + " x " + board.getDimension()
                + " = " + board.getSize() + " cells");
        System.out.println("Snakes and ladders count each: " + board.getDimension());

        for (Map.Entry<Integer, JumpStrategy> entry : board.getSpecialCells().entrySet()) {
            int start = entry.getKey();
            int end = entry.getValue().jump();

            if (entry.getValue() instanceof Snake) {
                System.out.println("Snake  : " + start + " -> " + end);
            } else if (entry.getValue() instanceof Ladder) {
                System.out.println("Ladder : " + start + " -> " + end);
            }
        }
    }
}