import java.util.ArrayList;
import java.util.Comparator;
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

    public void addProduct(Product product) throws DuplicateProductException {
        if (products.containsKey(product.getId())) {
            throw new DuplicateProductException("Товар з артикулом " + product.getId() + " вже існує.");
        }
        products.put(product.getId(), product);
    }

    public Product findProductById(String id) throws ProductNotFoundException {
        Product product = products.get(id);
        if (product == null) {
            throw new ProductNotFoundException("Товар з артикулом " + id + " не знайдено.");
        }
        return product;
    }

    public List<Product> getAllProducts() {
        return new ArrayList<>(products.values());
    }

    public void removeProduct(String id) throws ProductNotFoundException {
        if (products.remove(id) == null) {
            throw new ProductNotFoundException("Товар з артикулом " + id + " не вдалося видалити (не знайдено).");
        }
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
            try {
                this.addProduct(product);
            } catch (DuplicateProductException e) {
                System.out.println("Попередження при завантаженні: " + e.getMessage());
            }
        }
    }

    public List<Product> getSortedProducts(Comparator<Product> comparator) {
        List<Product> list = new ArrayList<>(products.values());
        list.sort(comparator);
        return list;
    }
}