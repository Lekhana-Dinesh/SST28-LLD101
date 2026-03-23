import java.util.List;

public class GameFactory {

    public Game createGame(List<Player> players,Board board,Dice dice,IMakeMoveStrategy moveStrategy) {
        return new Game(players, board, dice, moveStrategy);
    }
}