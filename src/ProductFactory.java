import java.time.LocalDate;

public class ProductFactory {
    /**
     * Створює об'єкт Product з рядка CSV.
     * Використовується DataManager.
     */

    public static Product createProductFromCsv(String[] parts) throws InvalidDataException {
        try {
            String type = parts[0];
            String id = parts[1];
            String name = parts[2];
            double price = Double.parseDouble(parts[3]);
            int quantity = Integer.parseInt(parts[4]);

            switch (type.toUpperCase()) {
                case "FOOD":
                    // parts[5] - це expirationDate
                    LocalDate date = LocalDate.parse(parts[5]);
                    return new FoodProduct(id, name, price, quantity, date);
                case "ELEC":
                    // parts[5] - це warrantyMonths
                    int warranty = Integer.parseInt(parts[5]);
                    return new ElectronicsProduct(id, name, price, quantity, warranty);
                case "CLOTHING":
                    // parts[5] - size, parts[6] - color
                    String size = parts[5];
                    String color = parts[6];
                    return new ClothingProduct(id, name, price, quantity, size, color);
                default:
                    throw new InvalidDataException("Невідомий тип товару у файлі: " + type);
            }
        } catch (Exception e) {
            throw new InvalidDataException("Помилка парсингу рядка CSV: " + e.getMessage());
        }
    }

}