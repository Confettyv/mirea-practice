package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Order {
    private final Employee employee;
    private final List<OrderItem> items;
    private OrderStatus status;

    public Order(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Сотрудник заказа не может быть null");
        }
        this.employee = employee;
        this.items = new ArrayList<>();
        this.status = OrderStatus.NEW;
    }

    public Employee getEmployee() {
        return employee;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public void addProduct(Product product, int quantity) {
        ensureModifiable();
        if (product == null) {
            throw new IllegalArgumentException("Товар не может быть null");
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
        ensureModifiable();
        if (productId == null || productId.isBlank()) {
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
        ensureModifiable();
        if (items.isEmpty()) {
            throw new IllegalStateException("Нельзя подтвердить пустой заказ");
        }
        this.status = OrderStatus.CONFIRMED;
    }

    private void ensureModifiable() {
        if (this.status == OrderStatus.CONFIRMED) {
            throw new IllegalStateException("Заказ уже подтвержден и не подлежит изменению");
        }
    }
}
