public class SelfCheck {
    public static void main(String[] args) {
        TimesheetAnalyzer analyzer = new TimesheetAnalyzer();
        System.out.println("=== Запуск проверок табеля (Вариант 1) ===");

        testCase(analyzer, "1. Типовое значение", "8;11;10;12;8", true);
        testCase(analyzer, "2. Нижняя граница (все 0)", "0;0;0;0;0", true);
        testCase(analyzer, "3. Верхняя граница (все 16)", "16;16;16;16;16", true);
        testCase(analyzer, "4. Пустая строка", "", false);
        testCase(analyzer, "5. Превышение максимума (17 часов в поз. 3)", "8;8;17;8;8", false);
        testCase(analyzer, "6. Неверный формат (буква в поз. 2)", "8;abc;8;8;8", false);
        testCase(analyzer, "7. Неверное число дней (4 дня)", "8;8;8;8", false);
    }

    private static void testCase(TimesheetAnalyzer analyzer, String title, String input, boolean expectedSuccess) {
        System.out.println("[" + title + "] Строка: '" + input + "'");
        try {
            int[] hours = analyzer.parseHours(input);
            String report = analyzer.buildReport(hours);
            System.out.println("  -> Успех: " + report);
            if (!expectedSuccess) {
                System.out.println("  -> ОШИБКА: ожидался сбой, но обработка прошла успешно!");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("  -> Контролируемая ошибка: " + e.getMessage());
            if (expectedSuccess) {
                System.out.println("  -> ОШИБКА: ожидался успешный разбор!");
            }
        }
    }
}
