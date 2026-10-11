import java.util.Locale;

public class SelfCheck {
    public static void main(String[] args) {
        runTests();
    }

    private static void runTests() {
        int passed = 0;
        int total = 0;

        // Тест 1: Валидность корректных данных
        total++;
        if (OrderCalculator.isValid(10, 100.0, 5.0, 10.0)) {
            passed++;
        } else {
            System.err.println("Тест 1 провален: данные должны быть валидны");
        }

        // Тест 2: Вычисление базовой стоимости
        total++;
        double base = OrderCalculator.calculateBase(10, 100.0);
        if (Math.abs(base - 1000.0) < 1e-6) {
            passed++;
        } else {
            System.err.println("Тест 2 провален: базовая сумма не равна 1000.0");
        }

        // Тест 3: Вычисление суммы со скидкой 10%
        total++;
        double discounted = OrderCalculator.applyDiscount(1000.0, 10.0);
        if (Math.abs(discounted - 900.0) < 1e-6) {
            passed++;
        } else {
            System.err.println("Тест 3 провален: сумма со скидкой не равна 900.0");
        }

        // Тест 4: Вычисление НДС 10% (вариант 1)
        total++;
        double vat = OrderCalculator.calculateVat(900.0, 10.0);
        if (Math.abs(vat - 90.0) < 1e-6) {
            passed++;
        } else {
            System.err.println("Тест 4 провален: НДС не равен 90.0");
        }

        // Тест 5: Полный расчет итоговой суммы (10 * 100 со скидкой 10% и НДС 10% -> 990.0)
        total++;
        double res = OrderCalculator.calculateTotal(10, 100.0, 10.0, 10.0);
        if (Math.abs(res - 990.0) < 1e-6) {
            passed++;
        } else {
            System.err.println("Тест 5 провален: итоговая сумма не равна 990.0");
        }

        // Тест 6: Граничная проверка - нулевое количество должно браковаться
        total++;
        if (!OrderCalculator.isValid(0, 100.0, 10.0, 10.0)) {
            passed++;
        } else {
            System.err.println("Тест 6 провален: нулевое количество должно быть невалидно");
        }

        // Тест 7: Граничная проверка - скидка более 30% должна браковаться
        total++;
        if (!OrderCalculator.isValid(5, 50.0, 30.1, 10.0)) {
            passed++;
        } else {
            System.err.println("Тест 7 провален: скидка 30.1% должна быть невалидна");
        }

        System.out.printf(Locale.US, "SelfCheck завершен: успешно %d из %d тестов.%n", passed, total);
    }
}
