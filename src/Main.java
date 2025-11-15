import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Warehouse warehouse = Warehouse.getInstance();
    private static final String DATA_FILE = "warehouse_data.csv";

    public static void main(String[] args) {
        System.out.println("=== Склад v1.0 ===");

        List<Product> savedProducts = DataManager.loadData(DATA_FILE);
        warehouse.loadProducts(savedProducts);

        boolean isRunning = true;
        while (isRunning) {
            printMenu();

            int choice = InputValidator.readInt(scanner, "Виберіть пункт: ");

            switch (choice) {
                case 1: addNewProduct(); break;
                case 2: showAllProducts(); break;
                case 3: deleteProduct(); break;
                case 4: updateProduct(); break;
                case 5: searchProduct(); break;
                case 6: sortProducts(); break;
                case 7:
                    isRunning = false;
                    DataManager.saveData(warehouse.getAllProducts(), DATA_FILE);
                    System.out.println("Вихід.");
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
        System.out.println("6. Сортувати товари");
        System.out.println("7. Вихід");
    }

    private static void addNewProduct() {
        System.out.println("\n--- Додавання нового товару ---");

        String id = InputValidator.readNonEmptyString(scanner, "Артикул: ");
        String name = InputValidator.readNonEmptyString(scanner, "Назва: ");
        double price = InputValidator.readDouble(scanner, "Ціна: ");

        int quantity;
        while (true) {
            quantity = InputValidator.readInt(scanner, "Кількість: ");
            if (quantity >= 0) break;
            System.out.println("Кількість не може бути від'ємною.");
        }

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
        String id = InputValidator.readNonEmptyString(scanner, "Введіть артикул: ");

        Product product = warehouse.findProductById(id);
        if (product == null) {
            System.out.println("Помилка: Товар не знайдено.");
            return;
        }

        System.out.println("Вибрано для видалення: " + product);
        String confirm = InputValidator.readNonEmptyString(scanner, "Ви впевнені? (так/ні): ").toLowerCase();

        if (confirm.equals("так") || confirm.equals("yes") || confirm.equals("y") || confirm.equals("+")) {
            boolean removed = warehouse.removeProduct(id);
            if (removed) {
                System.out.println("Товар успішно видалено.");
            } else {
                System.out.println("Помилка: Не вдалося видалити товар.");
            }
        } else {
            System.out.println("Видалення скасовано.");
        }
    }

    private static void updateProduct() {
        System.out.println("\n--- Оновлення товару ---");
        String id = InputValidator.readNonEmptyString(scanner, "Введіть артикул: ");

        Product product = warehouse.findProductById(id);
        if (product == null) {
            System.out.println("Товар не знайдено.");
            return;
        }

        System.out.println("Поточні дані: " + product);
        System.out.println("Натисніть Enter, щоб залишити без змін.");

        System.out.print("Нова назва [" + product.getName() + "]: ");
        String newName = scanner.nextLine().trim();
        if (!newName.isEmpty()) {
            product.setName(newName);
        }

        Double newPrice = InputValidator.readOptionalDouble(scanner, "Нова ціна [" + product.getPrice() + "]: ");
        if (newPrice != null) {
            product.setPrice(newPrice);
        }

        Integer newQty = InputValidator.readOptionalInt(scanner, "Нова кількість [" + product.getQuantity() + "]: ");
        if (newQty != null) {
            product.setQuantity(newQty);
        }

        System.out.println("Оновлено: " + product);
    }

    private static void searchProduct() {
        // ВАЛИДАЦИЯ
        String query = InputValidator.readNonEmptyString(scanner, "Пошук (артикул/назва): ");
        List<Product> res = warehouse.search(query);
        if(res.isEmpty()) System.out.println("Нічого не знайдено.");
        else res.forEach(System.out::println);
    }

    private static void sortProducts() {
        System.out.println("\n--- Сортування ---");
        System.out.println("1. За ціною");
        System.out.println("2. За кількістю");
        System.out.println("3. За назвою");
        System.out.println("4. За артикулом");

        int criterion = InputValidator.readInt(scanner, "Оберіть критерій: ");
        List<Product> sorted = null;

        switch (criterion) {
            case 1: sorted = warehouse.sortByPrice(); break;
            case 2: sorted = warehouse.sortByQuantity(); break;
            case 3: sorted = warehouse.sortByName(); break;
            case 4: sorted = warehouse.sortById(); break;
            default: System.out.println("Невірний критерій."); return;
        }

        if (sorted != null) sorted.forEach(System.out::println);
    }
}