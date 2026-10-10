package starter_project.src;

import java.util.Locale;
import java.util.Scanner;

public class OrderCalculator {
    public static final double VAT_RATE = 10.0; // Ставка НДС по Варианту 1
    public static final int MIN_QUANTITY = 1;
    public static final int MAX_QUANTITY = 10000;
    public static final double MIN_PRICE = 0.01;
    public static final double MAX_PRICE = 5000000.0;
    public static final double MIN_DISCOUNT = 0.0;
    public static final double MAX_DISCOUNT = 30.0;

    public static double calculateBase(int quantity, double price) {
        return quantity * price;
    }

    public static double applyDiscount(double baseAmount, double discountPercent) {
        return baseAmount * (1.0 - discountPercent / 100.0);
    }

    public static double calculateVat(double discountedAmount, double vatPercent) {
        return discountedAmount * (vatPercent / 100.0);
    }

    public static boolean isValid(int quantity, double price, double discountPercent, double vatPercent) {
        boolean qOk = quantity >= MIN_QUANTITY && quantity <= MAX_QUANTITY;
        boolean pOk = price >= MIN_PRICE && price <= MAX_PRICE;
        boolean dOk = discountPercent >= MIN_DISCOUNT && discountPercent <= MAX_DISCOUNT;
        boolean vOk = Math.abs(vatPercent - VAT_RATE) < 0.0001;
        return qOk && pOk && dOk && vOk;
    }

    public static String validateAndExplain(int quantity, double price, double discountPercent, double vatPercent) {
        if (quantity < MIN_QUANTITY || quantity > MAX_QUANTITY) {
            return "Ошибка: количество единиц должно быть в диапазоне от " + MIN_QUANTITY + " до " + MAX_QUANTITY + ". Получено: " + quantity;
        }
        if (price < MIN_PRICE || price > MAX_PRICE) {
            return "Ошибка: цена единицы должна быть от " + MIN_PRICE + " до " + MAX_PRICE + " руб. Получено: " + price;
        }
        if (discountPercent < MIN_DISCOUNT || discountPercent > MAX_DISCOUNT) {
            return "Ошибка: скидка должна быть в диапазоне от " + MIN_DISCOUNT + "% до " + MAX_DISCOUNT + "%. Получено: " + discountPercent;
        }
        if (Math.abs(vatPercent - VAT_RATE) >= 0.0001) {
            return "Ошибка: ставка НДС для Варианта 1 должна составлять " + VAT_RATE + "%. Получено: " + vatPercent;
        }
        return "Данные корректны";
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        scanner.useLocale(Locale.US);

        System.out.println("=== Расчёт параметров заказа (Вариант 1) ===");
        System.out.print("Введите количество единиц (целое число от 1 до 10000): ");
        if (!scanner.hasNextInt()) {
            System.out.println("Ошибка: введено не целое число.");
            return;
        }
        int quantity = scanner.nextInt();

        System.out.print("Введите цену за единицу (руб., от 0.01 до 5000000): ");
        if (!scanner.hasNextDouble()) {
            System.out.println("Ошибка: введено некорректное числовое значение цены.");
            return;
        }
        double price = scanner.nextDouble();

        System.out.print("Введите скидку (% от 0 до 30): ");
        if (!scanner.hasNextDouble()) {
            System.out.println("Ошибка: некорректное значение скидки.");
            return;
        }
        double discount = scanner.nextDouble();

        System.out.print("Введите ставку НДС (% 10.0 для Варианта 1): ");
        if (!scanner.hasNextDouble()) {
            System.out.println("Ошибка: некорректное значение ставки НДС.");
            return;
        }
        double vat = scanner.nextDouble();

        if (!isValid(quantity, price, discount, vat)) {
            System.out.println(validateAndExplain(quantity, price, discount, vat));
            return;
        }

        double baseAmount = calculateBase(quantity, price);
        double discountedAmount = applyDiscount(baseAmount, discount);
        double discountSum = baseAmount - discountedAmount;
        double vatAmount = calculateVat(discountedAmount, vat);
        double total = discountedAmount + vatAmount;

        System.out.println("\n--- Результаты расчёта ---");
        System.out.printf(Locale.US, "Базовая стоимость:       %12.2f руб.%n", baseAmount);
        System.out.printf(Locale.US, "Сумма скидки (%.1f%%):     %12.2f руб.%n", discount, discountSum);
        System.out.printf(Locale.US, "Сумма после скидки:      %12.2f руб.%n", discountedAmount);
        System.out.printf(Locale.US, "Сумма НДС (%.1f%%):        %12.2f руб.%n", vat, vatAmount);
        System.out.printf(Locale.US, "Итоговая сумма к оплате: %12.2f руб.%n", total);
    }
}
