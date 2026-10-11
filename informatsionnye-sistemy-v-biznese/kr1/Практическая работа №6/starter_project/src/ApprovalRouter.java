package starter_project.src;

public class ApprovalRouter {
    // Вариант 1: базовый порог 500 000 с учетом модификатора +10% равен 550 000
    public static final long DEFAULT_FINANCE_LIMIT = 550_000L;
    private final long financeLimit;

    public ApprovalRouter() {
        this(DEFAULT_FINANCE_LIMIT);
    }

    public ApprovalRouter(long financeLimit) {
        if (financeLimit < 0) {
            throw new IllegalArgumentException("Лимит согласования не может быть отрицательным");
        }
        this.financeLimit = financeLimit;
    }

    public long getFinanceLimit() {
        return financeLimit;
    }

    public ApprovalRoute route(long amount, boolean urgent, RiskLevel riskLevel, boolean hasPersonalData) {
        if (amount < 0) {
            throw new IllegalArgumentException("Сумма заявки не может быть отрицательной: " + amount);
        }
        if (riskLevel == null) {
            throw new IllegalArgumentException("Уровень риска не может быть null (отсутствует значение)");
        }

        // Разрешение конфликта/повтора: персональные данные перекрывают любые другие флаги
        if (hasPersonalData) {
            return ApprovalRoute.SECURITY_REVIEW;
        }
        if (riskLevel == RiskLevel.HIGH) {
            return ApprovalRoute.DIRECTOR_REVIEW;
        }
        if (amount >= financeLimit) {
            return ApprovalRoute.FINANCE_REVIEW;
        }
        if (urgent) {
            return ApprovalRoute.FAST_TRACK;
        }
        return ApprovalRoute.STANDARD;
    }
}
