import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Game {
    private final Queue<Player> activePlayers;
    private final List<Player> winners;
    private final Board board;
    private final Dice dice;
    private final IMakeMoveStrategy moveStrategy;

    private static final int MAX_TURNS = 10000;

    public Game(List<Player> playerList, Board board, Dice dice, IMakeMoveStrategy moveStrategy) {
        if (playerList == null || playerList.size() < 2) {
            throw new IllegalArgumentException("At least 2 players are required");
        }
        this.activePlayers = new LinkedList<>(playerList);
        this.winners = new ArrayList<>();
        this.board = board;
        this.dice = dice;
        this.moveStrategy = moveStrategy;
    }

    public void start() {
        System.out.println("Game started on a " + board.getDimension() + "x" + board.getDimension() + " board.");
        System.out.println();

        int turnCount = 0;

        while (activePlayers.size() >= 2) {
            if (turnCount >= MAX_TURNS) {
                System.out.println("Game ended: maximum turn limit (" + MAX_TURNS + ") reached.");
                break;
            }

            Player currentPlayer = activePlayers.poll();
            moveStrategy.makeMove(currentPlayer, board, dice);

            if (currentPlayer.win(board.getSize())) {
                winners.add(currentPlayer);
                System.out.println(currentPlayer.getName() + " finished at rank " + winners.size() + "!");
                System.out.println();
            } else {
                activePlayers.offer(currentPlayer);
            }

            turnCount++;
        }

        System.out.println("Game over.");
        if (!activePlayers.isEmpty()) {
            Player lastRemaining = activePlayers.poll();
            System.out.println("Last player still playing when game stopped: "
                    + lastRemaining.getName() + " at position " + lastRemaining.getPosition());
        }
        if (!winners.isEmpty()) {
            System.out.println("Winners order:");
            for (int i = 0; i < winners.size(); i++) {
                System.out.println((i + 1) + ". " + winners.get(i).getName());
            }
        } else {
            System.out.println("No player reached the last cell.");
        }
    }
}