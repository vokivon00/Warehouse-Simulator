public abstract class Product {
    // 2. ІНКАПСУЛЯЦІЯ: Поля залишаються приватними
    private String id;
    private String name;
    private double price;
    private int quantity;

    public Product(String id, String name, double price, int quantity) {
        if (id == null || id.trim().isEmpty()) {
            throw new InvalidDataException("Артикул не може бути порожнім.");
        }
        if (price < 0) {
            throw new InvalidDataException("Ціна не може бути від'ємною: " + price);
        }
        if (quantity < 0) {
            throw new InvalidDataException("Кількість не може бути від'ємною: " + quantity);
        }

        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public abstract String getFullDescription();

    public abstract String toCsvString();

    public String getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public void setName(String name) { this.name = name; }
    public void setPrice(double price) {
        if (price < 0) throw new InvalidDataException("Ціна не може бути від'ємною.");
        this.price = price;
    }
    public void setQuantity(int quantity) {
        if (quantity < 0) throw new InvalidDataException("Кількість не може бути від'ємною.");
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return String.format("Артикул: %-10s | Назва: %-20s | Ціна: %8.2f | К-сть: %d",
                id, name, price, quantity);
    }
}