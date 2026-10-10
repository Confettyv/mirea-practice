public class TimesheetAnalyzer {
    public static final int DAILY_NORM = 10;
    public static final int WEEKLY_NORM = 40;
    public static final int MIN_HOURS = 0;
    public static final int MAX_HOURS = 16;
    public static final int EXPECTED_DAYS = 5;

    public int[] parseHours(String line) {
        if (line == null || line.trim().isEmpty()) {
            throw new IllegalArgumentException("Ошибка: пустая или отсутствующая строка");
        }
        String[] parts = line.split(";", -1);
        if (parts.length != EXPECTED_DAYS) {
            throw new IllegalArgumentException("Ошибка: ожидается ровно 5 значений, получено " + parts.length);
        }
        int[] hours = new int[EXPECTED_DAYS];
        for (int i = 0; i < parts.length; i++) {
            String token = parts[i].trim();
            int val;
            try {
                val = Integer.parseInt(token);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Ошибка в позиции " + (i + 1) + ": не является целым числом ('" + token + "')");
            }
            if (val < MIN_HOURS || val > MAX_HOURS) {
                throw new IllegalArgumentException("Ошибка в позиции " + (i + 1) + ": значение " + val + " вне диапазона [0, 16]");
            }
            hours[i] = val;
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
        if (hours.length == 0) {
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
        int diff = total(hours) - WEEKLY_NORM;
        return diff > 0 ? diff : 0;
    }

    public String buildReport(int[] hours) {
        int tot = total(hours);
        double avg = average(hours);
        int otDays = overtimeDays(hours);
        int wOvertime = weeklyOvertime(hours);
        return String.format("Сумма=%d, Среднее=%.2f, Дней переработки (>%dч)=%d, Переработка недели=%d",
                tot, avg, DAILY_NORM, otDays, wOvertime);
    }
}
