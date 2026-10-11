public class TimesheetAnalyzer {

    public static final int EXPECTED_DAYS = 5;
    public static final int MIN_HOURS = 0;
    public static final int MAX_HOURS = 16;
    public static final int DAILY_NORM = 10; // Вариант 1: норма часов в день 10
    public static final int WEEKLY_NORM = 40;

    public static int[] parseHours(String line) {
        if (line == null || line.trim().isEmpty()) {
            throw new IllegalArgumentException("Ошибка: пустая или отсутствующая строка табеля.");
        }

        String[] tokens = line.split(";", -1);
        if (tokens.length != EXPECTED_DAYS) {
            throw new IllegalArgumentException(
                "Ошибка: ожидается ровно " + EXPECTED_DAYS + " значений через ';', получено: " + tokens.length
            );
        }

        int[] hours = new int[EXPECTED_DAYS];
        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i].trim();
            int value;
            try {
                value = Integer.parseInt(token);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(
                    "Ошибка на позиции " + (i + 1) + ": значение '" + token + "' не является целым числом."
                );
            }

            if (value < MIN_HOURS || value > MAX_HOURS) {
                throw new IllegalArgumentException(
                    "Ошибка на позиции " + (i + 1) + ": значение " + value + " выходит за диапазон [" + MIN_HOURS + "; " + MAX_HOURS + "]."
                );
            }
            hours[i] = value;
        }

        return hours;
    }

    public static int total(int[] hours) {
        int sum = 0;
        for (int i = 0; i < hours.length; i++) {
            sum += hours[i];
        }
        return sum;
    }

    public static double average(int[] hours) {
        if (hours.length == 0) {
            return 0.0;
        }
        return (double) total(hours) / hours.length;
    }

    public static int overtimeDays(int[] hours) {
        return overtimeDays(hours, DAILY_NORM);
    }

    public static int overtimeDays(int[] hours, int dailyNorm) {
        int count = 0;
        for (int i = 0; i < hours.length; i++) {
            if (hours[i] > dailyNorm) {
                count++;
            }
        }
        return count;
    }

    public static int weeklyOvertime(int[] hours) {
        int sum = total(hours);
        return sum > WEEKLY_NORM ? sum - WEEKLY_NORM : 0;
    }
}
