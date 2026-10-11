public class Product {
    private final String id;
    private final String name;
    private final double price;

    public Product(String id, String name, double price) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Product id cannot be empty");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
        if (price <= 0.0) {
            throw new IllegalArgumentException("Product price must be positive");
        }
        String cleanId = id.trim();
        ProductRegistry.register(cleanId);
        this.id = cleanId;
        this.name = name.trim();
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }
}
