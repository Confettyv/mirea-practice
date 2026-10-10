import model.BusinessTrip;
import model.Employee;
import model.TripStatus;
import service.BusinessTripService;

import java.time.LocalDate;
import java.util.List;

public class SelfCheck {
    private static int checksPassed = 0;
    private static int checksTotal = 0;

    public static void main(String[] args) {
        System.out.println("=== Запуск SelfCheck: Практическая работа №8 (Вариант 1) ===");

        Employee emp1 = new Employee("E1", "Иванов Иван", "Разработка");
        Employee emp2 = new Employee("E2", "Петров Петр", "Аналитика");
        BusinessTripService service = new BusinessTripService();

        testStandardFlow(emp1);
        testBoundaryCheckDates(emp1);
        testBoundaryCheckRejectionReason(emp1);
        testIllegalStateTransitions(emp1);
        testCancellationRules(emp2);
        testServiceFiltering(emp1, emp2, service);
        testNullOrBlankInputs(emp1);
        testInvalidDateFormat(service, emp1);
        testDuplicateConflict(service, emp1);

        System.out.println("--------------------------------------------------");
        System.out.println("Итог проверок: " + checksPassed + " из " + checksTotal + " успешно пройдено.");
        if (checksPassed == checksTotal) {
            System.out.println("Все обязательные правила и граничные условия выполнены корректно!");
        } else {
            System.err.println("Обнаружены ошибки при проверке инвариантов!");
        }
    }

    private static void testStandardFlow(Employee emp) {
        checksTotal++;
        LocalDate start = LocalDate.now().minusDays(5);
        LocalDate end = LocalDate.now().minusDays(2);
        BusinessTrip trip = new BusinessTrip("TR-OK", emp, start, end);

        trip.submit();
        trip.approve();
        trip.complete(LocalDate.now()); // строго после даты end

        if (trip.getStatus() == TripStatus.COMPLETED) {
            System.out.println("[OK] Проверка 1 (корректное типовое): Полный жизненный цикл DRAFT -> SUBMITTED -> APPROVED -> COMPLETED");
            checksPassed++;
        } else {
            System.out.println("[FAIL] Проверка 1: Неверный финальный статус: " + trip.getStatus());
        }
    }

    private static void testBoundaryCheckDates(Employee emp) {
        checksTotal++;
        LocalDate start = LocalDate.of(2026, 5, 10);
        LocalDate end = LocalDate.of(2026, 5, 9);
        boolean caught = false;
        try {
            new BusinessTrip("TR-DATE-ERR", emp, start, end);
        } catch (IllegalArgumentException e) {
            caught = true;
        }

        if (caught) {
            System.out.println("[OK] Проверка 2 (граничные даты): Нарушение интервала (start > end) пресечено с IllegalArgumentException");
            checksPassed++;
        } else {
            System.out.println("[FAIL] Проверка 2: Разрешено создание командировки с датой начала позже даты окончания");
        }
    }

    private static void testBoundaryCheckRejectionReason(Employee emp) {
        checksTotal++;
        LocalDate start = LocalDate.of(2026, 6, 1);
        LocalDate end = LocalDate.of(2026, 6, 5);
        BusinessTrip trip = new BusinessTrip("TR-REJ", emp, start, end);
        trip.submit();

        boolean shortReasonRejected = false;
        try {
            trip.reject("123456789"); // 9 символов (нижняя граница - 1)
        } catch (IllegalArgumentException e) {
            shortReasonRejected = true;
        }

        boolean exact10Accepted = false;
        try {
            trip.reject("1234567890"); // ровно 10 символов (точная нижняя граница включения)
            if (trip.getStatus() == TripStatus.REJECTED && trip.getRejectionReason().equals("1234567890")) {
                exact10Accepted = true;
            }
        } catch (Exception e) {
            exact10Accepted = false;
        }

        if (shortReasonRejected && exact10Accepted) {
            System.out.println("[OK] Проверка 3 (граничные длины): Причина < 10 отклонена, ровно 10 символов успешно принята");
            checksPassed++;
        } else {
            System.out.println("[FAIL] Проверка 3: Ошибка валидации длины причины отказа");
        }
    }

    private static void testIllegalStateTransitions(Employee emp) {
        checksTotal++;
        LocalDate start = LocalDate.of(2026, 7, 1);
        LocalDate end = LocalDate.of(2026, 7, 10);
        BusinessTrip trip = new BusinessTrip("TR-ILLEGAL", emp, start, end);

        boolean directApproveRejected = false;
        try {
            trip.approve(); // Нельзя одобрить напрямую из DRAFT
        } catch (IllegalStateException e) {
            directApproveRejected = true;
        }

        trip.submit();
        trip.approve();

        boolean onEndDateRejected = false;
        try {
            trip.complete(end); // Запрещено: должно быть строго ПОСЛЕ даты окончания
        } catch (IllegalStateException e) {
            onEndDateRejected = true;
        }

        if (directApproveRejected && onEndDateRejected) {
            System.out.println("[OK] Проверка 4 (недопустимые переходы): DRAFT->APPROVED и complete() в день end блокируются с IllegalStateException");
            checksPassed++;
        } else {
            System.out.println("[FAIL] Проверка 4: Недопустимый переход состояния не был заблокирован");
        }
    }

    private static void testCancellationRules(Employee emp) {
        checksTotal++;
        LocalDate start = LocalDate.now().minusDays(10);
        LocalDate end = LocalDate.now().minusDays(5);
        BusinessTrip trip = new BusinessTrip("TR-CANCEL", emp, start, end);
        trip.submit();
        trip.approve();
        trip.complete(LocalDate.now());

        boolean cancelAfterCompleteRejected = false;
        try {
            trip.cancel(); // Запрещено после COMPLETED
        } catch (IllegalStateException e) {
            cancelAfterCompleteRejected = true;
        }

        if (cancelAfterCompleteRejected) {
            System.out.println("[OK] Проверка 5 (запрет отмены): Отмена после COMPLETED блокируется с IllegalStateException");
            checksPassed++;
        } else {
            System.out.println("[FAIL] Проверка 5: Удалось отменить завершённую командировку");
        }
    }

    private static void testServiceFiltering(Employee emp1, Employee emp2, BusinessTripService service) {
        checksTotal++;
        BusinessTrip t1 = service.create("S-1", emp1, LocalDate.now(), LocalDate.now().plusDays(2));
        BusinessTrip t2 = service.create("S-2", emp2, LocalDate.now(), LocalDate.now().plusDays(3));

        t1.submit();
        t1.approve();

        List<BusinessTrip> approved = service.findByStatus(TripStatus.APPROVED);
        List<BusinessTrip> drafts = service.findByStatus(TripStatus.DRAFT);

        if (approved.size() == 1 && drafts.size() == 1 && approved.get(0).getId().equals("S-1")) {
            System.out.println("[OK] Проверка 6 (фильтрация сервиса): Выборка по статусам вернула корректные списки");
            checksPassed++;
        } else {
            System.out.println("[FAIL] Проверка 6: Сервис некорректно отфильтровал список заявок");
        }
    }

    private static void testNullOrBlankInputs(Employee emp) {
        checksTotal++;
        boolean nullEmpCaught = false;
        boolean blankIdCaught = false;
        boolean nullReasonCaught = false;

        try {
            new BusinessTrip("ID-NULL", null, LocalDate.now(), LocalDate.now().plusDays(1));
        } catch (IllegalArgumentException e) {
            nullEmpCaught = true;
        }

        try {
            new BusinessTrip("   ", emp, LocalDate.now(), LocalDate.now().plusDays(1));
        } catch (IllegalArgumentException e) {
            blankIdCaught = true;
        }

        BusinessTrip trip = new BusinessTrip("TR-BLANK", emp, LocalDate.now(), LocalDate.now().plusDays(1));
        trip.submit();
        try {
            trip.reject("   ");
        } catch (IllegalArgumentException e) {
            nullReasonCaught = true;
        }

        if (nullEmpCaught && blankIdCaught && nullReasonCaught) {
            System.out.println("[OK] Проверка 7 (пустые/отсутствующие значения): Контролируемая ошибка IllegalArgumentException при пустых полях");
            checksPassed++;
        } else {
            System.out.println("[FAIL] Проверка 7: Пропущены пустые/отсутствующие значения");
        }
    }

    private static void testInvalidDateFormat(BusinessTripService service, Employee emp) {
        checksTotal++;
        boolean caught = false;
        try {
            service.createFromStrings("TR-FMT-ERR", emp, "2026/05/10", "2026-05-15");
        } catch (IllegalArgumentException e) {
            caught = true;
        }

        if (caught) {
            System.out.println("[OK] Проверка 8 (неверный формат данных): Неверный формат даты вызвал контролируемый IllegalArgumentException");
            checksPassed++;
        } else {
            System.out.println("[FAIL] Проверка 8: Неверный строковый формат даты не был перехвачен");
        }
    }

    private static void testDuplicateConflict(BusinessTripService service, Employee emp) {
        checksTotal++;
        boolean caught = false;
        try {
            service.create("S-1", emp, LocalDate.now(), LocalDate.now().plusDays(1)); // S-1 уже создан в testServiceFiltering
        } catch (IllegalArgumentException e) {
            caught = true;
        }

        if (caught) {
            System.out.println("[OK] Проверка 9 (повтор/конфликт данных): Дубликат идентификатора командировки пресечён с IllegalArgumentException");
            checksPassed++;
        } else {
            System.out.println("[FAIL] Проверка 9: Разрешено повторное добавление существующего ID в сервис");
        }
    }
}
