import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DataManager {

    /**
     * Зберігає список товарів у файл CSV.
     */
    public static void saveData(List<Product> products, String filePath) {
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
            System.out.println("Дані успішно збережено у файл: " + filePath);
        } catch (IOException e) {
            System.err.println("Помилка при збереженні даних: " + e.getMessage());
        }
    }

    /**
     * Завантажує список товарів з файлу CSV.
     */
    public static List<Product> loadData(String filePath) {
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