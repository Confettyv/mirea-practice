import java.util.Locale;
import java.util.Scanner;

public class OrderCalculator {
    public static final double VAT_RATE = 10.0; // Вариант 1: ставка НДС 10%

    public static boolean isValid(int quantity, double price, double discount, double vatRate) {
        return quantity > 0 && quantity <= 10000
                && price > 0.0 && price <= 5000000.0
                && discount >= 0.0 && discount <= 30.0
                && vatRate >= 0.0 && vatRate <= 100.0;
    }

    public static String getValidationError(int quantity, double price, double discount, double vatRate) {
        if (quantity <= 0 || quantity > 10000) {
            return "Ошибка: количество единиц должно быть от 1 до 10000. Введено: " + quantity;
        }
        if (price <= 0.0 || price > 5000000.0) {
            return String.format(Locale.US, "Ошибка: цена за единицу должна быть от 0.01 до 5000000.00 руб. Введено: %.2f", price);
        }
        if (discount < 0.0 || discount > 30.0) {
            return String.format(Locale.US, "Ошибка: скидка должна быть в пределах от 0 до 30%%. Введено: %.2f", discount);
        }
        if (vatRate < 0.0 || vatRate > 100.0) {
            return String.format(Locale.US, "Ошибка: ставка НДС должна быть в пределах от 0 до 100%%. Введено: %.2f", vatRate);
        }
        return "";
    }

    public static double calculateBase(int quantity, double price) {
        return quantity * price;
    }

    public static double applyDiscount(double baseAmount, double discountPercent) {
        return baseAmount * (1.0 - discountPercent / 100.0);
    }

    public static double calculateVat(double discountedAmount, double vatRate) {
        return discountedAmount * (vatRate / 100.0);
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

        if (!scanner.hasNextInt()) {
            System.out.println("Ошибка ввода: количество должно быть целым числом.");
            return;
        }
        int quantity = scanner.nextInt();

        if (!scanner.hasNextDouble()) {
            System.out.println("Ошибка ввода: цена должна быть вещественным числом.");
            return;
        }
        double price = scanner.nextDouble();

        if (!scanner.hasNextDouble()) {
            System.out.println("Ошибка ввода: скидка должна быть вещественным числом.");
            return;
        }
        double discount = scanner.nextDouble();

        double vatRate = VAT_RATE;
        if (scanner.hasNextDouble()) {
            vatRate = scanner.nextDouble();
        }

        if (!isValid(quantity, price, discount, vatRate)) {
            System.out.println(getValidationError(quantity, price, discount, vatRate));
            return;
        }

        double baseAmount = calculateBase(quantity, price);
        double discountedAmount = applyDiscount(baseAmount, discount);
        double vatAmount = calculateVat(discountedAmount, vatRate);
        double totalAmount = discountedAmount + vatAmount;

        System.out.printf(Locale.US, "Базовая стоимость: %.2f руб.%n", baseAmount);
        System.out.printf(Locale.US, "Сумма со скидкой (%.2f%%): %.2f руб.%n", discount, discountedAmount);
        System.out.printf(Locale.US, "Сумма НДС (%.2f%%): %.2f руб.%n", vatRate, vatAmount);
        System.out.printf(Locale.US, "Итоговая сумма заказа: %.2f руб.%n", totalAmount);
    }
}
