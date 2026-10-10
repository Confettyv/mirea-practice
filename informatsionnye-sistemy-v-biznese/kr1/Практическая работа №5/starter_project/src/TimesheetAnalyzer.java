public class TimesheetAnalyzer {
    public static final int DAILY_NORM = 10; // Вариант 1: норма часов в день
    public static final int WEEKLY_NORM = 40;
    public static final int MIN_DAILY_HOURS = 0;
    public static final int MAX_DAILY_HOURS = 16;
    public static final int EXPECTED_DAYS = 5;

    public int[] parseHours(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("Ошибка: пустая строка табеля");
        }

        String[] tokens = input.split(";", -1);
        if (tokens.length != EXPECTED_DAYS) {
            throw new IllegalArgumentException(
                "Ошибка: неверное количество дней: " + tokens.length + " (ожидается " + EXPECTED_DAYS + ")"
            );
        }

        int[] hours = new int[EXPECTED_DAYS];
        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i].trim();
            int pos = i + 1;
            int value;
            try {
                value = Integer.parseInt(token);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Ошибка в позиции " + pos + ": значение '" + token + "' не является целым числом");
            }

            if (value < MIN_DAILY_HOURS || value > MAX_DAILY_HOURS) {
                throw new IllegalArgumentException(
                    "Ошибка в позиции " + pos + ": часы " + value + " выходят за допустимый диапазон [" + MIN_DAILY_HOURS + ", " + MAX_DAILY_HOURS + "]"
                );
            }
            hours[i] = value;
        }
        return hours;
    }

    public int total(int[] hours) {
        int sum = 0;
        for (int i = 0; i < hours.length; i++) {
            sum += hours[i];
        }
        return sum;
    }

    public double average(int[] hours) {
        if (hours == null || hours.length == 0) {
            return 0.0;
        }
        return (double) total(hours) / hours.length;
    }

    public int overtimeDays(int[] hours) {
        int count = 0;
        for (int i = 0; i < hours.length; i++) {
            if (hours[i] > DAILY_NORM) {
                count++;
            }
        }
        return count;
    }

    public int weeklyOvertime(int[] hours) {
        int sum = total(hours);
        int overtime = sum - WEEKLY_NORM;
        return overtime > 0 ? overtime : 0;
    }

    public String formatReport(int[] hours) {
        return String.format(
            "Сумма: %d ч, Среднее: %.1f ч/день, Дней переработки (>%d ч): %d, Недельная переработка: %d ч",
            total(hours), average(hours), DAILY_NORM, overtimeDays(hours), weeklyOvertime(hours)
        );
    }
}
