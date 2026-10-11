import java.util.Map;

public class OrderService {
    public Order createOrder(Employee employee) {
        return new Order(employee);
    }

    public void parseAndAddItem(Order order, String csvLine, Map<String, Product> catalog) {
        if (order == null) throw new IllegalArgumentException("Order cannot be null");
        if (csvLine == null || csvLine.trim().isEmpty()) throw new IllegalArgumentException("Line is empty");

        String[] parts = csvLine.split(",");
        if (parts.length != 4) {
            throw new IllegalArgumentException("Invalid CSV format: expected 4 fields, got " + parts.length);
        }

        String productId = parts[0].trim();
        int quantity;
        try {
            quantity = Integer.parseInt(parts[3].trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Malformed integer quantity: " + parts[3].trim());
        }

        Product product = catalog.get(productId);
        if (product == null) {
            throw new IllegalArgumentException("Product not found in catalog: " + productId);
        }
        order.addProduct(product, quantity);
    }

    public void validateForConfirm(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        if (order.getItems().isEmpty()) {
            throw new IllegalStateException("Order must contain at least one item before confirmation");
        }
    }

    public void confirmOrder(Order order) {
        validateForConfirm(order);
        order.confirm();
    }
}
