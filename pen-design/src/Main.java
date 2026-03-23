public class Main {
    public static void main(String[] args) {

        System.out.println("=== Ball Pen (click + standard refill) ===");
        Pen ballPen = new BallPen("Reynolds", InkColor.BLUE, 80);
        ballPen.click();
        ballPen.write("Hello from a ball pen!");
        ballPen.refill(25);
        ballPen.close();

        System.out.println("\n=== Gel Pen (click + cartridge refill) ===");
        Pen gelPen = new GelPen("Pilot", InkColor.BLACK, 50);
        gelPen.click();
        gelPen.write("Writing with a gel pen.");
        gelPen.refill(0); // invalid
        gelPen.refill(50); // cartridge replace → fills to 100%
        gelPen.close();

        System.out.println("\n=== Ink Pen (uncap + standard refill) ===");
        Pen inkPen = new InkPen("Parker", InkColor.BLUE, 60);
        inkPen.click();  // uncaps
        inkPen.write("Elegant writing with an ink pen.");
        inkPen.close();  // caps back
        inkPen.write("Cannot write — pen is closed.");
    }
}
