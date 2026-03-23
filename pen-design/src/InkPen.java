// Fountain/ink pen — cap on/off to open/close, standard refill
public class InkPen extends Pen {
    public InkPen(String brand, InkColor inkColor, int inkLevel) {
        super(brand, inkColor, inkLevel, new StandardRefillStrategy(), new UncapStrategy());
    }
}
