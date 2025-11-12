import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Warehouse warehouse = Warehouse.getInstance();
    private static final String FILE_PATH = "warehouse_data.csv";

    public static void main(String[] args) {
        System.out.println("=== Склад v1.0 ===");

        List<Product> loadedProducts = DataManager.loadData(FILE_PATH);
        warehouse.loadProducts(loadedProducts);

        boolean isRunning = true;
        while (isRunning) {
            printMenu();
            System.out.print("Виберіть пункт: ");

            String input = scanner.nextLine();
            if (input.isEmpty()) continue;
            int choice = Integer.parseInt(input);

            switch (choice) {
                case 1:
                    addNewProduct();
                    break;
                case 2:
                    showAllProducts();
                    break;
                case 3:
                    deleteProduct();
                    break;
                case 4:
                    updateProduct();
                    break;
                case 5:
                    searchProduct();
                    break;
                case 6:
                    System.out.println("Збереження даних...");
                    DataManager.saveData(warehouse.getAllProducts(), FILE_PATH);

                    isRunning = false;
                    System.out.println("Вихід. До побачення!");
                    break;
                default:
                    System.out.println("Невірний пункт меню.");
            }
        }

        scanner.close();
    }

    private static void printMenu() {
        System.out.println("\n--- Меню ---");
        System.out.println("1. Додати товар");
        System.out.println("2. Показати всі товари");
        System.out.println("3. Видалити товар");
        System.out.println("4. Оновити товар");
        System.out.println("5. Пошук товару");
        System.out.println("6. Вихід (Зберегти дані)"); // Обновлен текст
    }

    private static void addNewProduct() {
        System.out.println("\n--- Додавання нового товару ---");
        System.out.print("Артикул: ");
        String id = scanner.nextLine();
        System.out.print("Назва: ");
        String name = scanner.nextLine();
        System.out.print("Ціна: ");
        double price = Double.parseDouble(scanner.nextLine());
        System.out.print("Кількість: ");
        int quantity = Integer.parseInt(scanner.nextLine());

        Product product = new Product(id, name, price, quantity);
        if (warehouse.addProduct(product)) {
            System.out.println("Товар успішно додано!");
        } else {
            System.out.println("Помилка: Товар з таким артикулом вже існує.");
        }
    }

    private static void showAllProducts() {
        System.out.println("\n--- Всі товари на складі ---");
        List<Product> products = warehouse.getAllProducts();
        if (products.isEmpty()) {
            System.out.println("Склад порожній.");
        } else {
            for (Product p : products) {
                System.out.println(p);
            }
        }
    }

    private static void deleteProduct() {
        System.out.println("\n--- Видалення товару ---");
        System.out.print("Введіть артикул товару для видалення: ");
        String id = scanner.nextLine();

        Product product = warehouse.findProductById(id);
        if (product == null) {
            System.out.println("Товар з артикулом '" + id + "' не знайдено.");
            return;
        }

        System.out.println("Знайдено товар: " + product);
        System.out.print("Підтвердіть видалення (так/ні): ");
        String confirmation = scanner.nextLine().trim().toLowerCase();

        if (confirmation.equals("так") || confirmation.equals("yes")) {
            if (warehouse.removeProduct(id)) {
                System.out.println("Товар успішно видалено!");
            } else {
                System.out.println("Помилка при видаленні товару.");
            }
        } else {
            System.out.println("Видалення скасовано.");
        }
    }

    private static void updateProduct() {
        System.out.println("\n--- Оновлення товару ---");
        System.out.print("Введіть артикул товару для оновлення: ");
        String id = scanner.nextLine();

        Product product = warehouse.findProductById(id);
        if (product == null) {
            System.out.println("Товар з артикулом '" + id + "' не знайдено.");
            return;
        }

        System.out.println("Поточні дані товару: " + product);
        System.out.println("\nВведіть нові значення (або натисніть Enter для пропуску):");

        System.out.print("Нова назва [" + product.getName() + "]: ");
        String newName = scanner.nextLine().trim();
        if (!newName.isEmpty()) product.setName(newName);

        System.out.print("Нова ціна [" + product.getPrice() + "]: ");
        String newPriceStr = scanner.nextLine().trim();
        if (!newPriceStr.isEmpty()) product.setPrice(Double.parseDouble(newPriceStr));

        System.out.print("Нова кількість [" + product.getQuantity() + "]: ");
        String newQuantityStr = scanner.nextLine().trim();
        if (!newQuantityStr.isEmpty()) product.setQuantity(Integer.parseInt(newQuantityStr));

        System.out.println("Товар успішно оновлено!");
        System.out.println("Оновлені дані: " + product);
    }

    private static void searchProduct() {
        System.out.println("\n--- Пошук товару ---");
        System.out.print("Введіть артикул або частину назви: ");
        String query = scanner.nextLine().trim();

        if (query.isEmpty()) {
            System.out.println("Пошуковий запит порожній.");
            return;
        }

        List<Product> foundProducts = warehouse.search(query);

        if (foundProducts.isEmpty()) {
            System.out.println("Нічого не знайдено.");
        } else {
            System.out.println("Знайдено товарів: " + foundProducts.size());
            for (Product p : foundProducts) {
                System.out.println(p);
            }
        }
    }
}