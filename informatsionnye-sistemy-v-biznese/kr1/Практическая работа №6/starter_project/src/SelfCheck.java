package starter_project.src;

public class SelfCheck {
    public static void main(String[] args) {
        ApprovalRouter router = new ApprovalRouter();
        int passed = 0;
        int total = 0;

        // 1. Корректное типовое значение
        total++;
        if (router.route(100_000L, false, RiskLevel.LOW, false) == ApprovalRoute.STANDARD) passed++;

        // 2. Персональные данные при конфликте/повторе флагов
        total++;
        if (router.route(1_000_000L, true, RiskLevel.HIGH, true) == ApprovalRoute.SECURITY_REVIEW) passed++;

        // 3. Границы Варианта 1 (порог 550 000)
        total++;
        if (router.route(549_999L, true, RiskLevel.LOW, false) == ApprovalRoute.FAST_TRACK) passed++;
        total++;
        if (router.route(550_000L, true, RiskLevel.LOW, false) == ApprovalRoute.FINANCE_REVIEW) passed++;

        // 4. Отсутствующее/пустое значение (null riskLevel)
        total++;
        try {
            router.route(100_000L, false, null, false);
        } catch (IllegalArgumentException e) {
            passed++;
        }

        // 5. Значение неверного формата в парсере enuma
        total++;
        try {
            RiskLevel.parse("INVALID_FORMAT");
        } catch (IllegalArgumentException e) {
            passed++;
        }

        // 6. Отсутствующее значение в парсере enuma (пустая строка)
        total++;
        try {
            RiskLevel.parse("   ");
        } catch (IllegalArgumentException e) {
            passed++;
        }

        // 7. Отрицательная сумма
        total++;
        try {
            router.route(-500L, false, RiskLevel.LOW, false);
        } catch (IllegalArgumentException e) {
            passed++;
        }

        System.out.println("SelfCheck completed: " + passed + "/" + total + " tests passed.");
        if (passed != total) {
            System.exit(1);
        }
    }
}
