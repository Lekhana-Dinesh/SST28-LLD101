// Retractable ballpoint pen — click to open/close, standard refill
public class BallPen extends Pen {
    public BallPen(String brand, InkColor inkColor, int inkLevel) {
        super(brand, inkColor, inkLevel, new StandardRefillStrategy(), new ClickStrategy());
    }
}
