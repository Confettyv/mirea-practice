package starter_project.src;

public class SelfCheck {
    public static void main(String[] args) {
        runTests();
    }

    public static void runTests() {
        System.out.println("Запуск встроенных проверок SelfCheck...");
        int passed = 0;

        // Тест 1: Базовый расчёт
        double base = OrderCalculator.calculateBase(5, 1000.0);
        if (Math.abs(base - 5000.0) < 0.001) passed++;
        else System.err.println("Провал теста 1: base");

        // Тест 2: Применение скидки
        double discounted = OrderCalculator.applyDiscount(5000.0, 10.0);
        if (Math.abs(discounted - 4500.0) < 0.001) passed++;
        else System.err.println("Провал теста 2: discount");

        // Тест 3: Расчёт НДС 10%
        double vat = OrderCalculator.calculateVat(4500.0, 10.0);
        if (Math.abs(vat - 450.0) < 0.001) passed++;
        else System.err.println("Провал теста 3: vat");

        // Тест 4: Валидация корректных данных
        if (OrderCalculator.isValid(5, 1000.0, 10.0, 10.0)) passed++;
        else System.err.println("Провал теста 4: valid check");

        // Тест 5: Отрицательное количество
        if (!OrderCalculator.isValid(-1, 100.0, 5.0, 10.0)) passed++;
        else System.err.println("Провал теста 5: quantity < 0");

        // Тест 6: Превышение скидки
        if (!OrderCalculator.isValid(10, 100.0, 35.0, 10.0)) passed++;
        else System.err.println("Провал теста 6: discount > 30");

        // Тест 7: Неверная ставка НДС для Варианта 1
        if (!OrderCalculator.isValid(10, 100.0, 5.0, 20.0)) passed++;
        else System.err.println("Провал теста 7: vat != 10");

        System.out.println("Пройдено тестов: " + passed + " из 7.");
        if (passed == 7) {
            System.out.println("Все автоматические проверки успешно завершены!");
        } else {
            System.err.println("Обнаружены ошибки при проверке!");
        }
    }
}
