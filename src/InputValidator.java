import java.util.Scanner;

public class InputValidator {

    public static int readInt(Scanner scanner, String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Помилка: Введіть ціле число.");
            }
        }
    }

    public static double readDouble(Scanner scanner, String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim();
            try {
                double value = Double.parseDouble(input);
                if (value < 0) {
                    System.out.println("Помилка: Значення не може бути від'ємним.");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Помилка: Введіть число (наприклад, 10.50).");
            }
        }
    }

    public static String readNonEmptyString(Scanner scanner, String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Помилка: Значення не може бути порожнім.");
        }
    }

    /**
     * Читає ціле число. Якщо введено порожній рядок, повертає null.
     * Перевіряє, що число не від'ємне.
     */
    public static Integer readOptionalInt(Scanner scanner, String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                return null;
            }
            try {
                int value = Integer.parseInt(input);
                if (value < 0) {
                    System.out.println("Помилка: Кількість не може бути від'ємною.");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Помилка: Введіть ціле число.");
            }
        }
    }

    /**
     * Читає дробове число. Якщо введено порожній рядок, повертає null.
     * Перевіряє, що число не від'ємне.
     */
    public static Double readOptionalDouble(Scanner scanner, String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                return null;
            }
            try {
                double value = Double.parseDouble(input);
                if (value < 0) {
                    System.out.println("Помилка: Ціна не може бути від'ємною.");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Помилка: Введіть число (наприклад, 10.50).");
            }
        }
    }
}