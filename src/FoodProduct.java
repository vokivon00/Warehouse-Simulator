import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FoodProduct extends Product {

    private LocalDate expirationDate;

    public FoodProduct(String id, String name, double price, int quantity, LocalDate expirationDate) {
        super(id, name, price, quantity);
        this.expirationDate = expirationDate;
    }

    @Override
    public String getFullDescription() {
        return String.format(
                "Товар: %s (Харчовий продукт)\n" +
                        "  Артикул: %s\n" +
                        "  Ціна: %.2f грн\n" +
                        "  Кількість: %d шт.\n" +
                        "  Термін придатності: %s",
                getName(), getId(), getPrice(), getQuantity(),
                expirationDate.format(DateTimeFormatter.ISO_LOCAL_DATE)
        );
    }

    @Override
    public String toCsvString() {
        return String.format(java.util.Locale.US, "FOOD,%s,%s,%.2f,%d,%s",
                getId(), getName(), getPrice(), getQuantity(), expirationDate
        );
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }
}