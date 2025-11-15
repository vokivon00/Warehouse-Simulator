public class ClothingProduct extends Product {

    private String size;
    private String color;

    public ClothingProduct(String id, String name, double price, int quantity, String size, String color) {
        super(id, name, price, quantity);
        this.size = size;
        this.color = color;
    }

    @Override
    public String getFullDescription() {
        return String.format(
                "Товар: %s (Одяг)\n" +
                        "  Артикул: %s\n" +
                        "  Ціна: %.2f грн\n" +
                        "  Кількість: %d шт.\n" +
                        "  Розмір: %s, Колір: %s",
                getName(), getId(), getPrice(), getQuantity(), this.size, this.color
        );
    }

    @Override
    public String toCsvString() {
        return String.format(java.util.Locale.US, "CLOTHING,%s,%s,%.2f,%d,%s,%s",
                getId(), getName(), getPrice(), getQuantity(), size, color
        );
    }

    public String getSize() { return size; }
    public String getColor() { return color; }
}