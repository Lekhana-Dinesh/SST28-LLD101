public class Hard implements IMakeMoveStrategy {

    @Override
    public void makeMove(Player player, Board board, Dice dice) {
        int diceValue = dice.genRandNo();
        int currentPosition = player.getPosition();
        int tentativePosition = currentPosition + diceValue;
        System.out.println(player.getName() + " rolled: " + diceValue);
        if (tentativePosition > board.getSize()) {
            System.out.println(player.getName() + " loses this move.");
            return;
        }

        int finalPosition = board.resolvePosition(tentativePosition);
        printJumpMessage(tentativePosition, finalPosition, player.getName());
        player.setPosition(finalPosition);
        System.out.println(player.getName() + " moved to: " + finalPosition);
    }

    private void printJumpMessage(int tentativePosition, int finalPosition, String playerName) {
        if (finalPosition > tentativePosition) {
            System.out.println(playerName + " climbed a ladder from " + tentativePosition + " to " + finalPosition);
        } else if (finalPosition < tentativePosition) {
            System.out.println(playerName + " got bitten by a snake from " + tentativePosition + " to " + finalPosition);
        }
    }
}