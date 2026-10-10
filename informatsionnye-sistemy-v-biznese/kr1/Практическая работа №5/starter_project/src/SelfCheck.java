public class SelfCheck {
    private static final TimesheetAnalyzer analyzer = new TimesheetAnalyzer();

    public static void main(String[] args) {
        System.out.println("=== Запуск SelfCheck проверки табеля (Вариант 1) ===");

        // 1. Корректное типовое значение
        testCase("Типовой табель", "8;10;12;6;8", false);

        // 2. Нижняя граница (все нули)
        testCase("Нижняя граница (нули)", "0;0;0;0;0", false);

        // 3. Верхняя граница (максимум 16)
        testCase("Верхняя граница (все 16)", "16;16;16;16;16", false);

        // 4. Пустая строка
        testCase("Пустая строка", "", true);

        // 5. Превышение верхней границы (значение 17)
        testCase("Значение 17 в позиции 3", "8;8;17;8;8", true);

        // 6. Отрицательное число
        testCase("Отрицательные часы", "8;-1;8;8;8", true);

        // 7. Неверный формат (текст)
        testCase("Текстовый токен", "8;abc;8;8;8", true);

        // 8. Неверное количество дней (меньше 5)
        testCase("Мало дней", "8;8;8", true);

        // 9. Неверное количество дней (больше 5)
        testCase("Много дней", "8;8;8;8;8;8", true);

        System.out.println("=== Все тесты SelfCheck успешно пройдены ===");
    }

    private static void testCase(String label, String input, boolean expectError) {
        try {
            int[] hours = analyzer.parseHours(input);
            if (expectError) {
                System.err.println("[ПРОВАЛ] " + label + " -> ожидалась ошибка, но строка успешно разобрана!");
            } else {
                System.out.println("[УСПЕХ] " + label + " -> " + analyzer.formatReport(hours));
            }
        } catch (IllegalArgumentException e) {
            if (expectError) {
                System.out.println("[УСПЕХ] " + label + " -> перехвачена ошибка: " + e.getMessage());
            } else {
                System.err.println("[ПРОВАЛ] " + label + " -> непредвиденная ошибка: " + e.getMessage());
            }
        }
    }
}
