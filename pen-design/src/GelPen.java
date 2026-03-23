// Gel pen — click to open/close, cartridge replacement for refill
public class GelPen extends Pen {
    public GelPen(String brand, InkColor inkColor, int inkLevel) {
        super(brand, inkColor, inkLevel, new CartridgeRefillStrategy(), new ClickStrategy());
    }
}
