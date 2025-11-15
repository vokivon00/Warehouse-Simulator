public class ElectronicsProduct extends Product {

    private int warrantyMonths;

    public ElectronicsProduct(String id, String name, double price, int quantity, int warrantyMonths) {
        super(id, name, price, quantity);
        if (warrantyMonths < 0) {
            throw new InvalidDataException("Гарантія не може бути від'ємною.");
        }
        this.warrantyMonths = warrantyMonths;
    }

    @Override
    public String getFullDescription() {
        return String.format(
                "Товар: %s (Електроніка)\n" +
                        "  Артикул: %s\n" +
                        "  Ціна: %.2f грн\n" +
                        "  Кількість: %d шт.\n" +
                        "  Гарантія: %d міс.",
                getName(), getId(), getPrice(), getQuantity(), this.warrantyMonths
        );
    }

    @Override
    public String toCsvString() {
        return String.format(java.util.Locale.US, "ELEC,%s,%s,%.2f,%d,%d",
                getId(), getName(), getPrice(), getQuantity(), warrantyMonths
        );
    }

    public int getWarrantyMonths() {
        return warrantyMonths;
    }
}