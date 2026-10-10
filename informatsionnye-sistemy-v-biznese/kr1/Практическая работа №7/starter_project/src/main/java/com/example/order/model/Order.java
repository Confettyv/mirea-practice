package com.example.order.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Order {
    private final Employee employee;
    private final List<OrderItem> items;
    private OrderStatus status;

    public Order(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Employee cannot be null for an order.");
        }
        this.employee = employee;
        this.items = new ArrayList<>();
        this.status = OrderStatus.NEW;
    }

    public Employee getEmployee() {
        return employee;
    }

    public List<OrderItem> getItems() {
        List<OrderItem> copies = items.stream().map(OrderItem::copy).collect(Collectors.toList());
        return Collections.unmodifiableList(copies);
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void addProduct(Product product, int quantity) {
        if (status != OrderStatus.NEW) {
            throw new IllegalStateException("Cannot modify a confirmed order.");
        }
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null.");
        }
        if (quantity < OrderItem.MIN_QUANTITY || quantity > OrderItem.MAX_QUANTITY) {
            throw new IllegalArgumentException("Quantity must be between " + OrderItem.MIN_QUANTITY + " and " + OrderItem.MAX_QUANTITY + ".");
        }

        Optional<OrderItem> existing = items.stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (existing.isPresent()) {
            existing.get().increase(quantity);
        } else {
            items.add(new OrderItem(product, quantity));
        }
    }

    public void removeProduct(String productId) {
        if (status != OrderStatus.NEW) {
            throw new IllegalStateException("Cannot modify a confirmed order.");
        }
        if (productId == null || productId.trim().isEmpty()) {
            throw new IllegalArgumentException("Product ID cannot be null or empty.");
        }
        boolean removed = items.removeIf(item -> item.getProduct().getId().equals(productId.trim()));
        if (!removed) {
            throw new IllegalArgumentException("Product with ID " + productId + " not found in order.");
        }
    }

    public double total() {
        return items.stream().mapToDouble(OrderItem::getLineTotal).sum();
    }

    public void confirm() {
        if (status != OrderStatus.NEW) {
            throw new IllegalStateException("Order is already confirmed or cancelled.");
        }
        if (items.isEmpty()) {
            throw new IllegalStateException("Cannot confirm an empty order.");
        }
        this.status = OrderStatus.CONFIRMED;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Order for ").append(employee.getFullName()).append(" (ID: ").append(employee.getId()).append(")\n");
        sb.append("Status: ").append(status).append("\nItems:\n");
        for (OrderItem item : items) {
            sb.append("  - ").append(item.getProduct().getName())
              .append(", Qty: ").append(item.getQuantity())
              .append(", Price: ").append(String.format("%.2f", item.getProduct().getPrice()))
              .append(", Line: ").append(String.format("%.2f", item.getLineTotal())).append("\n");
        }
        sb.append("Total: ").append(String.format("%.2f", total())).append("\n");
        return sb.toString();
    }
}
