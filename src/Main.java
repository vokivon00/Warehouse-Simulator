import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Warehouse warehouse = Warehouse.getInstance();
    private static final String DATA_FILE = "warehouse_data.csv";

    public static void main(String[] args) {
        System.out.println("=== Склад v2.0 (OOP) ===");
        loadData(); // Завантаження даних на старті

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
                    saveData(); // Збереження даних при виході
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
        System.out.println("2. Показати всі товари (з деталями)");
        System.out.println("3. Видалити товар");
        System.out.println("4. Оновити товар (базові поля)");
        System.out.println("5. Пошук товару");
        System.out.println("6. Сортувати товари");
        System.out.println("7. Зберегти та вийти");
    }


    private static void loadData() {
        try {
            List<Product> savedProducts = DataManager.loadData(DATA_FILE);
            warehouse.loadProducts(savedProducts);
            System.out.println("Дані успішно завантажено. Товарів на складі: " + savedProducts.size());
        } catch (IOException e) {
            System.out.println("!!! Критична помилка завантаження даних.");
            System.out.println("!!! " + e.getMessage());
            System.out.println("!!! Роботу буде продовжено з порожнім складом.");
            warehouse.loadProducts(new ArrayList<>());
        }
    }

    private static void saveData() {
        try {
            DataManager.saveData(warehouse.getAllProducts(), DATA_FILE);
            System.out.println("Дані успішно збережено.");
        } catch (IOException e) {
            System.out.println("!!! КРИТИЧНА ПОМИЛКА ЗБЕРЕЖЕННЯ ДАНИХ.");
            System.out.println("!!! " + e.getMessage());
            System.out.println("!!! Зміни можуть бути втрачені.");
        }
    }

    private static void addNewProduct() {
        System.out.println("\n--- Додавання нового товару ---");
        System.out.println("Оберіть тип товару:");
        System.out.println("1. Харчовий продукт");
        System.out.println("2. Електроніка");
        System.out.println("3. Одяг");
        int typeChoice = InputValidator.readInt(scanner, "Ваш вибір: ");

        try {
            String id = InputValidator.readNonEmptyString(scanner, "Артикул: ");
            String name = InputValidator.readNonEmptyString(scanner, "Назва: ");
            double price = InputValidator.readDouble(scanner, "Ціна: ");
            int quantity = InputValidator.readInt(scanner, "Кількість: ");

            Product newProduct = null;


            switch (typeChoice) {
                case 1:
                    LocalDate date = InputValidator.readDate(scanner, "Термін придатності (РРРР-ММ-ДД): ");
                    newProduct = new FoodProduct(id, name, price, quantity, date);
                    break;
                case 2:
                    int warranty = InputValidator.readInt(scanner, "Гарантія (міс.): ");
                    newProduct = new ElectronicsProduct(id, name, price, quantity, warranty);
                    break;
                case 3:
                    String size = InputValidator.readNonEmptyString(scanner, "Розмір (напр. L, 42, 180cm): ");
                    String color = InputValidator.readNonEmptyString(scanner, "Колір: ");
                    newProduct = new ClothingProduct(id, name, price, quantity, size, color);
                    break;
                default:
                    System.out.println("Помилка: Невідомий тип товару.");
                    return;
            }

            warehouse.addProduct(newProduct);
            System.out.println("Товар успішно додано!");

        } catch (InvalidDataException | DuplicateProductException e) {
            System.out.println("!!! Помилка додавання товару: " + e.getMessage());
        }
    }

    private static void showAllProducts() {
        System.out.println("\n--- Всі товари на складі (детально) ---");
        List<Product> products = warehouse.getAllProducts();
        if (products.isEmpty()) {
            System.out.println("Склад порожній.");
        } else {
            for (Product p : products) {
                System.out.println(p.getFullDescription());
                System.out.println("--------------------");
            }
        }
    }

    private static void deleteProduct() {
        System.out.println("\n--- Видалення товару ---");
        String id = InputValidator.readNonEmptyString(scanner, "Введіть артикул: ");

        try {
            Product product = warehouse.findProductById(id);
            System.out.println("Вибрано для видалення: " + product.toString());
            String confirm = InputValidator.readNonEmptyString(scanner, "Ви впевнені? (так/ні): ").toLowerCase();

            if (confirm.equals("так") || confirm.equals("yes") || confirm.equals("y") || confirm.equals("+")) {
                warehouse.removeProduct(id);
                System.out.println("Товар успішно видалено.");
            } else {
                System.out.println("Видалення скасовано.");
            }
        } catch (ProductNotFoundException e) {
            System.out.println("!!! Помилка: " + e.getMessage());
        }
    }

    private static void updateProduct() {
        System.out.println("\n--- Оновлення товару ---");
        String id = InputValidator.readNonEmptyString(scanner, "Введіть артикул: ");

        try {
            Product product = warehouse.findProductById(id);
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

        } catch (ProductNotFoundException e) {
            System.out.println("!!! Помилка: " + e.getMessage());
        } catch (InvalidDataException e) {
            System.out.println("!!! Помилка оновлення: " + e.getMessage());
        }
    }

    private static void searchProduct() {
        System.out.println("\n--- Пошук товару ---");
        String query = InputValidator.readNonEmptyString(scanner, "Пошук (артикул/назва): ");
        List<Product> res = warehouse.search(query);
        if(res.isEmpty()) {
            System.out.println("Нічого не знайдено.");
        } else {
            System.out.println("Знайдено " + res.size() + " товар(ів):");
            res.forEach(System.out::println);
        }
    }

    private static void sortProducts() {
        System.out.println("\n--- Сортування ---");
        System.out.println("1. За ціною");
        System.out.println("2. За кількістю");
        System.out.println("3. За назвою");
        System.out.println("4. За артикулом");

        int criterion = InputValidator.readInt(scanner, "Оберіть критерій: ");

        Comparator<Product> comparator = null;

        switch (criterion) {
            case 1:
                comparator = Comparator.comparingDouble(Product::getPrice);
                break;
            case 2:
                comparator = Comparator.comparingInt(Product::getQuantity);
                break;
            case 3:
                comparator = Comparator.comparing(p -> p.getName().toLowerCase());
                break;
            case 4:
                comparator = Comparator.comparing(Product::getId);
                break;
            default:
                System.out.println("Невірний критерій.");
                return;
        }

        List<Product> sorted = warehouse.getSortedProducts(comparator);

        System.out.println("--- Відсортований список ---");
        sorted.forEach(System.out::println);
    }
}