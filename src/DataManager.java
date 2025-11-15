import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DataManager {

    /**
     * Зберігає список товарів у файл CSV.
     * Може "кинути" IOException, якщо виникне помилка запису.
     */
    public static void saveData(List<Product> products, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("id,name,price,quantity");
            writer.newLine();

            for (Product product : products) {
                String line = String.format(Locale.US, "%s,%s,%.2f,%d",
                        escapeCsv(product.getId()),
                        escapeCsv(product.getName()),
                        product.getPrice(),
                        product.getQuantity());
                writer.write(line);
                writer.newLine();
            }
            // Повідомлення про успіх тепер краще перенести до Main,
            // але поки залишимо тут для простоти.
            System.out.println("Дані успішно збережено у файл: " + filePath);
        } catch (IOException e) {
            // Замість того, щоб просто видрукувати помилку,
            // ми "прокидаємо" її тому, хто викликав цей метод.
            System.err.println("Помилка при збереженні даних: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Завантажує список товарів з файлу CSV.
     * Може "кинути" IOException, якщо виникне помилка читання.
     */
    public static List<Product> loadData(String filePath) throws IOException {
        List<Product> products = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println("Файл даних не знайдено. Створено новий склад.");
            return products;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length >= 4) {
                    try {
                        String id = parts[0].trim().replace("\"", "");
                        String name = parts[1].trim().replace("\"", "");

                        double price = Double.parseDouble(parts[2].trim());
                        int quantity = Integer.parseInt(parts[3].trim());

                        products.add(new Product(id, name, price, quantity));
                    } catch (NumberFormatException e) {
                        System.err.println("Помилка парсингу рядка (пропущено): " + line);
                    }
                }
            }
            System.out.println("Завантажено товарів: " + products.size());
        } catch (IOException e) {
            System.err.println("Помилка при завантаженні даних: " + e.getMessage());
            throw e;
        }
        return products;
    }

    private static String escapeCsv(String value) {
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}