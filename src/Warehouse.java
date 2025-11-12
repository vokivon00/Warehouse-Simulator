import java.util.ArrayList;
import java.util.Comparator; // ВАЖНО: Добавлен импорт
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Warehouse {
    private static Warehouse instance;
    private final Map<String, Product> products;

    private Warehouse() {
        this.products = new HashMap<>();
    }

    public static Warehouse getInstance() {
        if (instance == null) {
            instance = new Warehouse();
        }
        return instance;
    }

    public boolean addProduct(Product product) {
        if (products.containsKey(product.getId())) {
            return false;
        }
        products.put(product.getId(), product);
        return true;
    }

    public Product findProductById(String id) {
        return products.get(id);
    }

    public List<Product> getAllProducts() {
        return new ArrayList<>(products.values());
    }

    public boolean removeProduct(String id) {
        return products.remove(id) != null;
    }

    public List<Product> search(String query) {
        List<Product> result = new ArrayList<>();
        String lowerQuery = query.toLowerCase();

        for (Product product : products.values()) {
            if (product.getId().toLowerCase().contains(lowerQuery) ||
                    product.getName().toLowerCase().contains(lowerQuery)) {
                result.add(product);
            }
        }
        return result;
    }

    public void loadProducts(List<Product> newProducts) {
        products.clear();
        for (Product product : newProducts) {
            products.put(product.getId(), product);
        }
    }

    public List<Product> sortByName() {
        List<Product> list = new ArrayList<>(products.values());
        list.sort(Comparator.comparing(p -> p.getName().toLowerCase()));
        return list;
    }

    public List<Product> sortByPrice() {
        List<Product> list = new ArrayList<>(products.values());
        list.sort(Comparator.comparingDouble(Product::getPrice));
        return list;
    }

    public List<Product> sortByQuantity() {
        List<Product> list = new ArrayList<>(products.values());
        list.sort(Comparator.comparingInt(Product::getQuantity));
        return list;
    }
}