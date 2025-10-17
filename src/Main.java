import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Warehouse warehouse = Warehouse.getInstance();

    public static void main(String[] args) {
        System.out.println("=== Склад v1.0 ===");

        boolean isRunning = true;
        while (isRunning) {
            printMenu();
            System.out.print("Виберіть пункт: ");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    addNewProduct();
                    break;
                case 2:
                    showAllProducts();
                    break;
                case 3:
                case 4:
                    System.out.println("Функція в розробці.");
                    break;
                case 5:
                    isRunning = false;
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
        System.out.println("5. Вихід");
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
}