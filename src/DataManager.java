import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataManager {
    public static void saveData(List<Product> products, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("type,id,name,price,quantity,extra1,extra2...");
            writer.newLine();

            for (Product product : products) {
                String line = product.toCsvString();
                writer.write(line);
                writer.newLine();
            }
        }
        // Не кидаємо помилку назовні, Main обробляє IOException
    }

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
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");

                try {
                    Product product = ProductFactory.createProductFromCsv(parts);
                    products.add(product);
                } catch (InvalidDataException e) {
                    // Ловимо нашу власну помилку, якщо рядок у файлі "битий"
                    System.err.println("Помилка парсингу рядка (пропущено): " + line);
                    System.err.println("  Причина: " + e.getMessage());
                }
            }
        }
        return products;
    }
}