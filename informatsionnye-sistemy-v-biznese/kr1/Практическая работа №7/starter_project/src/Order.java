import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Order {
    private final Employee employee;
    private final List<OrderItem> items;
    private OrderStatus status;

    public Order(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Employee cannot be null");
        }
        this.employee = employee;
        this.items = new ArrayList<>();
        this.status = OrderStatus.CREATED;
    }

    public Employee getEmployee() {
        return employee;
    }

    public List<OrderItem> getItems() {
        List<OrderItem> defensiveCopies = new ArrayList<>();
        for (OrderItem item : items) {
            defensiveCopies.add(new OrderItem(item.getProduct(), item.getQuantity()));
        }
        return Collections.unmodifiableList(defensiveCopies);
    }

    public OrderStatus getStatus() {
        return status;
    }

    private void checkNotConfirmed() {
        if (status == OrderStatus.CONFIRMED) {
            throw new IllegalStateException("Cannot modify confirmed order");
        }
    }

    public void addProduct(Product product, int quantity) {
        checkNotConfirmed();
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        for (OrderItem item : items) {
            if (item.getProduct().getId().equals(product.getId())) {
                item.increase(quantity);
                return;
            }
        }
        items.add(new OrderItem(product, quantity));
    }

    public boolean removeProduct(String productId) {
        checkNotConfirmed();
        if (productId == null || productId.trim().isEmpty()) {
            return false;
        }
        return items.removeIf(item -> item.getProduct().getId().equals(productId.trim()));
    }

    public double total() {
        double sum = 0.0;
        for (OrderItem item : items) {
            sum += item.getLineTotal();
        }
        return sum;
    }

    public void confirm() {
        checkNotConfirmed();
        if (items.isEmpty()) {
            throw new IllegalStateException("Cannot confirm empty order");
        }
        this.status = OrderStatus.CONFIRMED;
    }
}
