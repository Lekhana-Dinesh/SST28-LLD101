public interface InvoiceStore {
    void save(String invId, String printable);
    int countLines(String invId);
}
