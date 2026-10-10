import java.util.Locale;

public class SelfCheck {
    public static boolean runTests() {
        boolean allPassed = true;

        // Тест 1: Стандартный расчет
        double total1 = OrderCalculator.calculateTotal(10, 100.0, 10.0, 0.10);
        // base = 1000, discount = 10% -> 900, vat 10% -> 90, total = 990.00
        if (Math.abs(total1 - 990.0) > 1e-4) {
            System.out.println("Тест 1 провален: ожидалось 990.0, получено " + total1);
            allPassed = false;
        }

        // Тест 2: Нулевая скидка
        double total2 = OrderCalculator.calculateTotal(2, 500.0, 0.0, 0.10);
        // base = 1000, discount = 0 -> 1000, vat 10% -> 100, total = 1100.00
        if (Math.abs(total2 - 1100.0) > 1e-4) {
            System.out.println("Тест 2 провален: ожидалось 1100.0, получено " + total2);
            allPassed = false;
        }

        // Тест 3: Проверка нижней границы
        if (!OrderCalculator.isValid(1, 0.01, 0.0)) {
            System.out.println("Тест 3 (нижняя граница) провален: корректные значения признаны невалидными.");
            allPassed = false;
        }

        // Тест 4: Проверка верхней границы
        if (!OrderCalculator.isValid(10000, 5000000.0, 30.0)) {
            System.out.println("Тест 4 (верхняя граница) провален: граничные значения признаны невалидными.");
            allPassed = false;
        }

        // Тест 5: Невалидные значения
        if (OrderCalculator.isValid(0, 100.0, 5.0) ||
            OrderCalculator.isValid(1, -1.0, 5.0) ||
            OrderCalculator.isValid(1, 100.0, 30.5)) {
            System.out.println("Тест 5 (невалидные данные) провален: некорректные значения признаны валидными.");
            allPassed = false;
        }

        return allPassed;
    }

    public static void main(String[] args) {
        System.out.println("Запуск встроенного самотестирования...");
        boolean success = runTests();
        if (success) {
            System.out.println("SelfCheck: Все тесты пройдены успешно!");
        } else {
            System.out.println("SelfCheck: Обнаружены ошибки!");
            System.exit(1);
        }
    }
}
