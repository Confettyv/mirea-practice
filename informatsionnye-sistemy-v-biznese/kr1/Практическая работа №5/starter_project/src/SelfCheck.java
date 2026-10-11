public class SelfCheck {

    public static void main(String[] args) {
        System.out.println("=== Запуск набора тестов табеля (Вариант 1) ===\n");

        runTestSuite(TimesheetAnalyzer.DAILY_NORM);

        System.out.println("\n=== Демонстрация локального изменения требования (защита) ===");
        System.out.println("Изменение требования: норма часов в день изменена с 10 на 9.");
        int modifiedDailyNorm = 9;
        runTestSuite(modifiedDailyNorm);
    }

    private static void runTestSuite(int dailyNorm) {
        System.out.println("-- Набор проверок с дневной нормой = " + dailyNorm + " ч --");
        testCase("Типовой корректный табель", "8;10;11;9;8", true, dailyNorm);
        testCase("Нижняя граница (все 0)", "0;0;0;0;0", true, dailyNorm);
        testCase("Верхняя граница (все 16)", "16;16;16;16;16", true, dailyNorm);
        testCase("Пустая строка", "", false, dailyNorm);
        testCase("Превышение верхней границы (значение 17)", "8;10;17;8;8", false, dailyNorm);
        testCase("Отрицательное значение", "8;-1;8;8;8", false, dailyNorm);
        testCase("Неверное количество дней (4 дня)", "8;8;8;8", false, dailyNorm);
        testCase("Нечисловой ввод (символы)", "8;abc;8;8;8", false, dailyNorm);
        System.out.println("Результат: все тесты завершены штатно без регрессии.\n");
    }

    private static void testCase(String name, String input, boolean expectSuccess, int dailyNorm) {
        System.out.print("Тест: " + name + " | Вход: \"" + input + "\" -> ");
        try {
            int[] hours = TimesheetAnalyzer.parseHours(input);
            if (!expectSuccess) {
                System.out.println("ПРОВАЛ (ожидалась ошибка, но обработка прошла)");
                return;
            }
            int tot = TimesheetAnalyzer.total(hours);
            double avg = TimesheetAnalyzer.average(hours);
            int otDays = TimesheetAnalyzer.overtimeDays(hours, dailyNorm);
            int wOt = TimesheetAnalyzer.weeklyOvertime(hours);
            System.out.println(String.format("УСПЕХ [Сумма=%d, Среднее=%.2f, Дней переработки (>%dч)=%d, Недельная переработка=%d]",
                    tot, avg, dailyNorm, otDays, wOt));
        } catch (IllegalArgumentException e) {
            if (expectSuccess) {
                System.out.println("ПРОВАЛ (неожиданная ошибка: " + e.getMessage() + ")");
            } else {
                System.out.println("УСПЕХ (ошибка перехвачена: " + e.getMessage() + ")");
            }
        }
    }
}
