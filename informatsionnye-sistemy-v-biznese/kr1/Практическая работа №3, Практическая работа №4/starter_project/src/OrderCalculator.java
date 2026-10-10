import java.util.Locale;
import java.util.Scanner;

public class OrderCalculator {
    public static final double VAT_RATE = 0.10; // Вариант 1: ставка НДС 10%
    public static final int MIN_QUANTITY = 1;
    public static final int MAX_QUANTITY = 10000;
    public static final double MIN_PRICE = 0.0;
    public static final double MAX_PRICE = 5000000.0;
    public static final double MIN_DISCOUNT = 0.0;
    public static final double MAX_DISCOUNT = 30.0;

    public static boolean isValid(int quantity, double price, double discountPercent) {
        return quantity >= MIN_QUANTITY && quantity <= MAX_QUANTITY
                && price > MIN_PRICE && price <= MAX_PRICE
                && discountPercent >= MIN_DISCOUNT && discountPercent <= MAX_DISCOUNT;
    }

    public static String getValidationError(int quantity, double price, double discountPercent) {
        if (quantity < MIN_QUANTITY || quantity > MAX_QUANTITY) {
            return "Количество единиц должно быть в диапазоне от " + MIN_QUANTITY + " до " + MAX_QUANTITY + ".";
        }
        if (price <= MIN_PRICE || price > MAX_PRICE) {
            return "Цена единицы должна быть строго положительной и не более " + String.format(Locale.US, "%.2f", MAX_PRICE) + " руб.";
        }
        if (discountPercent < MIN_DISCOUNT || discountPercent > MAX_DISCOUNT) {
            return "Скидка должна быть в диапазоне от " + MIN_DISCOUNT + "% до " + MAX_DISCOUNT + "%.";
        }
        return "";
    }

    public static double calculateBase(int quantity, double price) {
        return quantity * price;
    }

    public static double applyDiscount(double baseSum, double discountPercent) {
        return baseSum * (1.0 - (discountPercent / 100.0));
    }

    public static double calculateVat(double taxableBase, double vatRate) {
        return taxableBase * vatRate;
    }

    public static double calculateTotal(int quantity, double price, double discountPercent, double vatRate) {
        double base = calculateBase(quantity, price);
        double discounted = applyDiscount(base, discountPercent);
        double vat = calculateVat(discounted, vatRate);
        return discounted + vat;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        scanner.useLocale(Locale.US);

        System.out.println("=== Расчет стоимости заказа (Вариант 1: НДС 10%) ===");
        System.out.print("Введите количество единиц: ");
        if (!scanner.hasNextInt()) {
            System.out.println("Ошибка: введено некорректное число для количества.");
            return;
        }
        int quantity = scanner.nextInt();

        System.out.print("Введите цену за единицу (руб): ");
        if (!scanner.hasNextDouble()) {
            System.out.println("Ошибка: введено некорректное значение цены.");
            return;
        }
        double price = scanner.nextDouble();

        System.out.print("Введите скидку (%): ");
        if (!scanner.hasNextDouble()) {
            System.out.println("Ошибка: введено некорректное значение скидки.");
            return;
        }
        double discount = scanner.nextDouble();

        if (!isValid(quantity, price, discount)) {
            System.out.println("Ошибка валидации: " + getValidationError(quantity, price, discount));
            return;
        }

        double baseSum = calculateBase(quantity, price);
        double discountedSum = applyDiscount(baseSum, discount);
        double discountAmount = baseSum - discountedSum;
        double vatAmount = calculateVat(discountedSum, VAT_RATE);
        double total = discountedSum + vatAmount;

        System.out.println("\n--- Результаты расчета ---");
        System.out.printf(Locale.US, "Базовая стоимость:        %12.2f руб.%n", baseSum);
        System.out.printf(Locale.US, "Сумма скидки (%.1f%%):     %12.2f руб.%n", discount, discountAmount);
        System.out.printf(Locale.US, "Сумма после скидки:       %12.2f руб.%n", discountedSum);
        System.out.printf(Locale.US, "НДС (%.0f%%):               %12.2f руб.%n", VAT_RATE * 100.0, vatAmount);
        System.out.printf(Locale.US, "Итоговая сумма к оплате:  %12.2f руб.%n", total);
    }
}
