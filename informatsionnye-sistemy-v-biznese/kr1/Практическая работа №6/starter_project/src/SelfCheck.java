package starter_project.src;

public class SelfCheck {
    public static void main(String[] args) {
        ApprovalRouter router = new ApprovalRouter();
        int passed = 0;
        int total = 0;

        total++;
        if (router.route(100_000, false, RiskLevel.LOW, true) == ApprovalRoute.SECURITY_REVIEW) passed++;

        total++;
        if (router.route(1_000_000, true, RiskLevel.HIGH, true) == ApprovalRoute.SECURITY_REVIEW) passed++;

        total++;
        if (router.route(100_000, true, RiskLevel.HIGH, false) == ApprovalRoute.DIRECTOR_REVIEW) passed++;

        total++;
        if (router.route(600_000, false, RiskLevel.HIGH, false) == ApprovalRoute.DIRECTOR_REVIEW) passed++;

        total++;
        if (router.route(500_000, false, RiskLevel.LOW, false) == ApprovalRoute.FINANCE_REVIEW) passed++;

        total++;
        if (router.route(500_001, false, RiskLevel.MEDIUM, false) == ApprovalRoute.FINANCE_REVIEW) passed++;

        total++;
        if (router.route(500_000, true, RiskLevel.LOW, false) == ApprovalRoute.FINANCE_REVIEW) passed++;

        total++;
        if (router.route(499_999, true, RiskLevel.LOW, false) == ApprovalRoute.FAST_TRACK) passed++;

        total++;
        if (router.route(499_999, false, RiskLevel.LOW, false) == ApprovalRoute.STANDARD) passed++;

        total++;
        if (router.route(0, false, RiskLevel.MEDIUM, false) == ApprovalRoute.STANDARD) passed++;

        total++;
        try {
            router.route(-10, false, RiskLevel.LOW, false);
        } catch (IllegalArgumentException e) {
            passed++;
        }

        total++;
        try {
            router.route(100, false, null, false);
        } catch (IllegalArgumentException e) {
            passed++;
        }

        System.out.println("Тестов пройдено: " + passed + " из " + total);
        if (passed == total) {
            System.out.println("Все проверки успешно пройдены!");
        } else {
            System.out.println("Обнаружены ошибки маршрутизации.");
        }
    }
}
