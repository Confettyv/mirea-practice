package com.example.order.model;

public class OrderItem {
    public static final int MIN_QUANTITY = 1;
    public static final int MAX_QUANTITY = 10; // Вариант 1: максимум 10 единиц

    private final Product product;
    private int quantity;

    public OrderItem(Product product, int quantity) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null.");
        }
        if (quantity < MIN_QUANTITY || quantity > MAX_QUANTITY) {
            throw new IllegalArgumentException("Quantity must be between " + MIN_QUANTITY + " and " + MAX_QUANTITY + ".");
        }
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void increase(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Increase amount must be positive.");
        }
        int newQuantity = this.quantity + amount;
        if (newQuantity > MAX_QUANTITY) {
            throw new IllegalArgumentException("Total quantity exceeds maximum allowed (" + MAX_QUANTITY + ").");
        }
        this.quantity = newQuantity;
    }

    public double getLineTotal() {
        return product.getPrice() * quantity;
    }

    public OrderItem copy() {
        return new OrderItem(this.product, this.quantity);
    }

    @Override
    public String toString() {
        return "OrderItem{product=" + product.getName() + ", qty=" + quantity + ", lineTotal=" + getLineTotal() + "}";
    }
}
