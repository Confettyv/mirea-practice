package starter_project;

public class SelfCheck {

    public static void main(String[] args) {
        ApprovalRouter router = new ApprovalRouter(); // Эффективный порог 550_000 (порог 500_000 + 10%)
        int passed = 0;
        int total = 0;

        // 1. Проверка наивысшего приоритета персональных данных
        total++;
        if (router.route(1_000_000L, true, RiskLevel.HIGH, true) == ApprovalRoute.SECURITY_REVIEW) passed++;

        // 2. Проверка персональных данных при минимальной сумме и низком риске
        total++;
        if (router.route(100L, false, RiskLevel.LOW, true) == ApprovalRoute.SECURITY_REVIEW) passed++;

        // 3. Проверка приоритета HIGH риска над суммой и срочностью
        total++;
        if (router.route(800_000L, true, RiskLevel.HIGH, false) == ApprovalRoute.DIRECTOR_REVIEW) passed++;

        // 4. Проверка границы 550 000 (FINANCE_REVIEW включительно при модификаторе 10)
        total++;
        if (router.route(550_000L, true, RiskLevel.LOW, false) == ApprovalRoute.FINANCE_REVIEW) passed++;

        // 5. Проверка границы 550 001 (FINANCE_REVIEW)
        total++;
        if (router.route(550_001L, false, RiskLevel.MEDIUM, false) == ApprovalRoute.FINANCE_REVIEW) passed++;

        // 6. Проверка границы 549 999 со срочностью (FAST_TRACK)
        total++;
        if (router.route(549_999L, true, RiskLevel.LOW, false) == ApprovalRoute.FAST_TRACK) passed++;

        // 7. Проверка границы 549 999 без срочности (STANDARD)
        total++;
        if (router.route(549_999L, false, RiskLevel.LOW, false) == ApprovalRoute.STANDARD) passed++;

        // 8. Проверка выброса контролируемого исключения на отрицательную сумму
        total++;
        try {
            router.route(-10L, false, RiskLevel.LOW, false);
        } catch (IllegalArgumentException e) {
            passed++;
        }

        // 9. Проверка выброса контролируемого исключения на отсутствующий/null riskLevel
        total++;
        try {
            router.route(1000L, false, null, false);
        } catch (IllegalArgumentException e) {
            passed++;
        }

        // 10. Проверка пустого или отсутствующего значения (строка null или пустая)
        total++;
        try {
            router.routeFromCsvLine("   ");
        } catch (IllegalArgumentException e) {
            passed++;
        }

        // 11. Проверка значения неверного формата (нечисловая сумма)
        total++;
        try {
            router.routeFromCsvLine("invalid_amount,true,LOW,false");
        } catch (IllegalArgumentException e) {
            passed++;
        }

        // 12. Проверка значения неверного формата (некорректный enum риска)
        total++;
        try {
            router.routeFromCsvLine("10000,true,UNKNOWN_RISK,false");
        } catch (IllegalArgumentException e) {
            passed++;
        }

        // 13. Проверка идемпотентности при повторе идентичных заявок
        total++;
        ApprovalRoute r1 = router.routeFromCsvLine("550000,true,LOW,false");
        ApprovalRoute r2 = router.routeFromCsvLine("550000,true,LOW,false");
        if (r1 == ApprovalRoute.FINANCE_REVIEW && r1 == r2) passed++;

        System.out.println("Самопроверка завершена: успешно " + passed + " из " + total);
        if (passed != total) {
            System.err.println("Обнаружены ошибки логики!");
            System.exit(1);
        }
    }
}
