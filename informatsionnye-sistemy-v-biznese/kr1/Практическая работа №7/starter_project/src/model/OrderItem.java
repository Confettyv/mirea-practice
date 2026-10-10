package model;

public class OrderItem {
    public static final int MIN_QUANTITY = 1;
    public static final int MAX_QUANTITY = 10; // Вариант 1: лимит позиции равен 10

    private final Product product;
    private int quantity;

    public OrderItem(Product product, int quantity) {
        if (product == null) {
            throw new IllegalArgumentException("Товар позиции не может быть null");
        }
        validateQuantity(quantity);
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void increase(int delta) {
        if (delta <= 0) {
            throw new IllegalArgumentException("Приращение количества должно быть положительным: " + delta);
        }
        int newQuantity = this.quantity + delta;
        validateQuantity(newQuantity);
        this.quantity = newQuantity;
    }

    public double getLineTotal() {
        return this.product.getPrice() * this.quantity;
    }

    private static void validateQuantity(int q) {
        if (q < MIN_QUANTITY || q > MAX_QUANTITY) {
            throw new IllegalArgumentException("Количество единиц должно быть от " 
                    + MIN_QUANTITY + " до " + MAX_QUANTITY + ", получено: " + q);
        }
    }
}
