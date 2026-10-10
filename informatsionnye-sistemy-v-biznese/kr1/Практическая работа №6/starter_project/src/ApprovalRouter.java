package starter_project.src;

public class ApprovalRouter {
    public static final long DEFAULT_FINANCE_LIMIT = 500_000L;
    private final long financeLimit;

    public ApprovalRouter() {
        this(DEFAULT_FINANCE_LIMIT);
    }

    public ApprovalRouter(long financeLimit) {
        if (financeLimit <= 0) {
            throw new IllegalArgumentException("Порог финансового контроля должен быть положительным");
        }
        this.financeLimit = financeLimit;
    }

    public ApprovalRoute route(long amount, boolean urgent, RiskLevel riskLevel, boolean containsPersonalData) {
        validateInput(amount, riskLevel);

        if (containsPersonalData) {
            return ApprovalRoute.SECURITY_REVIEW;
        }
        if (riskLevel == RiskLevel.HIGH) {
            return ApprovalRoute.DIRECTOR_REVIEW;
        }
        if (amount >= this.financeLimit) {
            return ApprovalRoute.FINANCE_REVIEW;
        }
        if (urgent) {
            return ApprovalRoute.FAST_TRACK;
        }
        return ApprovalRoute.STANDARD;
    }

    private void validateInput(long amount, RiskLevel riskLevel) {
        if (amount < 0) {
            throw new IllegalArgumentException("Сумма заявки не может быть отрицательной: " + amount);
        }
        if (riskLevel == null) {
            throw new IllegalArgumentException("Уровень риска заявки не может быть null");
        }
    }

    public long getFinanceLimit() {
        return financeLimit;
    }
}
