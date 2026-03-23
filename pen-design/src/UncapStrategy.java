public class UncapStrategy implements OpenCloseStrategy {

    @Override
    public void open() {
        System.out.println("Cap removed. Pen is ready to write.");
    }

    @Override
    public void close() {
        System.out.println("Cap placed back on pen.");
    }
}
