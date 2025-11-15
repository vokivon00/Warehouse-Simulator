import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Цей клас містить юніт-тести для валідації архітектури
 * та бізнес-логіки системи "Склад".
 */
class WarehouseTest {

    private Warehouse warehouse;
    @BeforeEach
    void setUp() throws Exception {
        Field instance = Warehouse.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);

        warehouse = Warehouse.getInstance();
    }

    /**
     * Тест 1: Перевірка цілісності даних (Integrität даних та Unchecked Exception)
     */
    @Test
    void test_ProductConstructor_Throws_InvalidDataException_On_NegativePrice() {
        assertThrows(InvalidDataException.class, () -> {
            new FoodProduct("F01", "Хліб", -100, 50, LocalDate.now());
        });
    }

    /**
     * Тест 2: Перевірка породжувального шаблону (Factory Method)
     */
    @Test
    void test_ProductFactory_Creates_Correct_Product_Type() {
        String[] parts = {"ELEC", "E01", "Laptop", "1500.0", "10", "24"};
        Product product = ProductFactory.createProductFromCsv(parts);

        assertNotNull(product);
        assertTrue(product instanceof ElectronicsProduct);
        assertFalse(product instanceof FoodProduct);
    }

    /**
     * Тест 3: Перевірка поліморфізму
     */
    @Test
    void test_Polymorphic_GetFullDescription_Returns_SpecificData() {
        Product food = new FoodProduct("F01", "Молоко", 40, 1, LocalDate.of(2025, 12, 1));
        Product electronics = new ElectronicsProduct("E01", "Телефон", 5000, 1, 12);

        String foodDescription = food.getFullDescription();
        String electronicsDescription = electronics.getFullDescription();

        assertTrue(foodDescription.contains("2025-12-01"));
        assertFalse(foodDescription.contains("Гарантія"));
        assertTrue(electronicsDescription.contains("Гарантія: 12 міс."));
        assertFalse(electronicsDescription.contains("2025-12-01"));
    }

    /**
     * Тест 4: Перевірка логіки Warehouse (Duplicate Entity)
     */
    @Test
    void test_Warehouse_addProduct_Throws_DuplicateProductException() {
        Product product1 = new ClothingProduct("C01", "Футболка", 300, 1, "L", "Білий");
        warehouse.addProduct(product1);

        Product product2 = new ElectronicsProduct("C01", "Тостер", 500, 1, 6);

        assertThrows(DuplicateProductException.class, () -> {
            warehouse.addProduct(product2);
        });
    }

    /**
     * Тест 5: Перевірка логіки Warehouse (Checked Exception)
     */
    @Test
    void test_Warehouse_findProductById_Throws_ProductNotFoundException() {
        assertThrows(ProductNotFoundException.class, () -> {
            warehouse.findProductById("ID-якого-не-існує");
        });
    }

    /**
     * Тест 6: Перевірка поведінкового шаблону (Strategy)
     */
    @Test
    void test_Warehouse_getSortedProducts_Strategy_Works() {
        Product p_middle = new ElectronicsProduct("E01", "Мишка", 500, 1, 12);
        Product p_cheap = new FoodProduct("F01", "Вода", 20, 1, LocalDate.now());
        Product p_expensive = new ElectronicsProduct("E02", "Клавіатура", 1000, 1, 24);

        warehouse.addProduct(p_middle);
        warehouse.addProduct(p_cheap);
        warehouse.addProduct(p_expensive);

        Comparator<Product> priceStrategy = Comparator.comparingDouble(Product::getPrice);
        List<Product> sortedList = warehouse.getSortedProducts(priceStrategy);

        assertEquals(3, sortedList.size());
        assertEquals("F01", sortedList.get(0).getId()); // Найдешевший
        assertEquals("E01", sortedList.get(1).getId()); // Середній
        assertEquals("E02", sortedList.get(2).getId()); // Найдорожчий
    }
}