public class Main {
    public static void main(String[] args) {
        Pen pen = new Pen("Pilot", InkColor.BLUE, 80);

        pen.start();
        pen.write("Hello, this is my pen design implementation.");
        pen.refill(25);
        pen.write("Writing again after refill.");
        pen.close();
    }
}