public class ClickStrategy implements OpenCloseStrategy {

    @Override
    public void open() {
        System.out.println("Click! Pen tip extended.");
    }

    @Override
    public void close() {
        System.out.println("Click! Pen tip retracted.");
    }
}
