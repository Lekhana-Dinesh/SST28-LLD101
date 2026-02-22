public abstract class Exporter {
    public final ExportResult export(ExportRequest req) {
        ExportRequest n = normalize(req);
        return doExport(n);
    }

    protected abstract ExportResult doExport(ExportRequest req);
    protected ExportRequest normalize(ExportRequest req) {
        if (req == null) {
            throw new IllegalArgumentException("request is null");
        }
        String title = (req.title == null) ? "" : req.title;
        String body  = (req.body == null)  ? "" : req.body;
        return new ExportRequest(title, body);
    }
}
