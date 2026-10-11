public class OrderItem {
    public static final int MIN_QUANTITY = 1;
    public static final int MAX_QUANTITY = 10; // Вариант 1

    private final Product product;
    private int quantity;

    public OrderItem(Product product, int quantity) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        validateQuantity(quantity);
        this.product = product;
        this.quantity = quantity;
    }

    private void validateQuantity(int q) {
        if (q < MIN_QUANTITY || q > MAX_QUANTITY) {
            throw new IllegalArgumentException("Quantity " + q + " is out of bounds [" + MIN_QUANTITY + ", " + MAX_QUANTITY + "]");
        }
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void increase(int additionalQuantity) {
        if (additionalQuantity <= 0) {
            throw new IllegalArgumentException("Added quantity must be positive");
        }
        int newQuantity = this.quantity + additionalQuantity;
        if (newQuantity > MAX_QUANTITY) {
            throw new IllegalStateException("Total item quantity " + newQuantity + " exceeds maximum limit " + MAX_QUANTITY);
        }
        this.quantity = newQuantity;
    }

    public double getLineTotal() {
        return product.getPrice() * quantity;
    }
}
