package starter_project;

public class ApprovalRouter {

    // Базовый порог финансового контроля
    public static final long BASE_FINANCE_LIMIT = 500_000L;
    // Модификатор варианта 1: параметр 10 (10% к порогу согласования)
    public static final int DEFAULT_MODIFIER_PERCENT = 10;

    private final long effectiveFinanceLimit;

    public ApprovalRouter() {
        this(DEFAULT_MODIFIER_PERCENT);
    }

    public ApprovalRouter(int modifierPercent) {
        if (modifierPercent < -100) {
            throw new IllegalArgumentException("Модификатор не может уменьшать порог более чем на 100%");
        }
        this.effectiveFinanceLimit = BASE_FINANCE_LIMIT * (100 + modifierPercent) / 100;
    }

    public long getEffectiveFinanceLimit() {
        return effectiveFinanceLimit;
    }

    public ApprovalRoute route(long amount, boolean urgent, RiskLevel riskLevel, boolean hasPersonalData) {
        validateInputs(amount, riskLevel);

        // Приоритет 1: Персональные данные
        if (hasPersonalData) {
            return ApprovalRoute.SECURITY_REVIEW;
        }
        // Приоритет 2: Высокий риск
        if (riskLevel == RiskLevel.HIGH) {
            return ApprovalRoute.DIRECTOR_REVIEW;
        }
        // Приоритет 3: Превышение финансового порога с учетом модификатора
        if (amount >= effectiveFinanceLimit) {
            return ApprovalRoute.FINANCE_REVIEW;
        }
        // Приоритет 4: Срочная заявка при сумме ниже порога
        if (urgent) {
            return ApprovalRoute.FAST_TRACK;
        }
        // Общий случай
        return ApprovalRoute.STANDARD;
    }

    // Парсинг и валидация строки данных (CSV/текст) с контролируемой обработкой ошибок формата и отсутствующих значений
    public ApprovalRoute routeFromCsvLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            throw new IllegalArgumentException("Входная строка заявки пуста или отсутствует");
        }
        String[] parts = line.split(",");
        if (parts.length < 4) {
            throw new IllegalArgumentException("Неверный формат входных данных заявки: ожидается 4 поля, получено " + parts.length);
        }
        long amount;
        try {
            amount = Long.parseLong(parts[0].trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Неверный формат суммы заявки: " + parts[0].trim(), e);
        }
        boolean urgent = parseBooleanStrict(parts[1].trim());
        RiskLevel riskLevel;
        try {
            riskLevel = RiskLevel.valueOf(parts[2].trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Неверный формат уровня риска: " + parts[2].trim(), e);
        }
        boolean hasPersonalData = parseBooleanStrict(parts[3].trim());
        return route(amount, urgent, riskLevel, hasPersonalData);
    }

    private boolean parseBooleanStrict(String val) {
        if ("true".equalsIgnoreCase(val)) return true;
        if ("false".equalsIgnoreCase(val)) return false;
        throw new IllegalArgumentException("Неверный формат логического значения: " + val);
    }

    private void validateInputs(long amount, RiskLevel riskLevel) {
        if (amount < 0) {
            throw new IllegalArgumentException("Сумма заявки не может быть отрицательной: " + amount);
        }
        if (riskLevel == null) {
            throw new IllegalArgumentException("Уровень риска должен быть указан");
        }
    }
}
