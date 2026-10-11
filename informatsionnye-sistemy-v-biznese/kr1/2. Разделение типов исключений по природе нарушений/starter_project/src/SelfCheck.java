import model.BusinessTrip;
import model.Employee;
import model.TripStatus;
import service.BusinessTripService;

import java.time.LocalDate;
import java.util.List;

public class SelfCheck {
    public static void main(String[] args) {
        System.out.println("=== Запуск комплексной самопроверки BusinessTrip ===");
        BusinessTripService service = new BusinessTripService();
        Employee emp = new Employee("E-1", "Иванов И.И.", "Разработка");

        // 1. Успешный полный цикл: DRAFT -> SUBMITTED -> APPROVED -> COMPLETED
        BusinessTrip trip1 = service.create("BT-01", emp, LocalDate.of(2025, 3, 1), LocalDate.of(2025, 3, 5));
        service.submitTrip("BT-01");
        service.approveTrip("BT-01");
        service.completeTrip("BT-01", LocalDate.of(2025, 3, 5));
        assertExpectation(trip1.getStatus() == TripStatus.COMPLETED, "Тест 1. Успешное завершение");

        // 2. Успешное отклонение: DRAFT -> SUBMITTED -> REJECTED (причина не менее 10 символов)
        BusinessTrip trip2 = service.create("BT-02", emp, LocalDate.of(2025, 3, 10), LocalDate.of(2025, 3, 15));
        service.submitTrip("BT-02");
        service.rejectTrip("BT-02", "1234567890");
        assertExpectation(trip2.getStatus() == TripStatus.REJECTED, "Тест 2. Успешное отклонение (длина 10)");

        // 3. Граничная проверка: отклонение с причиной 9 символов должно падать
        BusinessTrip trip3 = service.create("BT-03", emp, LocalDate.of(2025, 3, 10), LocalDate.of(2025, 3, 15));
        service.submitTrip("BT-03");
        boolean rejectedShort = false;
        try {
            service.rejectTrip("BT-03", "123456789");
        } catch (IllegalArgumentException e) {
            rejectedShort = true;
        }
        assertExpectation(rejectedShort && trip3.getStatus() == TripStatus.SUBMITTED, "Тест 3. Граница 9 символов отклонена");

        // 4. Недопустимый переход: попытка завершить заявку со статусом SUBMITTED
        boolean illegalTransition = false;
        try {
            service.completeTrip("BT-03", LocalDate.of(2025, 3, 20));
        } catch (IllegalStateException e) {
            illegalTransition = true;
        }
        assertExpectation(illegalTransition && trip3.getStatus() == TripStatus.SUBMITTED, "Тест 4. Недопустимый переход перехвачен");

        // 5. Граничная проверка даты: завершение за день до окончания поездки
        BusinessTrip trip5 = service.create("BT-05", emp, LocalDate.of(2025, 4, 1), LocalDate.of(2025, 4, 10));
        service.submitTrip("BT-05");
        service.approveTrip("BT-05");
        boolean earlyComplete = false;
        try {
            service.completeTrip("BT-05", LocalDate.of(2025, 4, 9));
        } catch (IllegalArgumentException e) {
            earlyComplete = true;
        }
        assertExpectation(earlyComplete && trip5.getStatus() == TripStatus.APPROVED, "Тест 5. Досрочное завершение запрещено");

        // 6. Отмена из APPROVED и запрет отмены из COMPLETED
        service.cancelTrip("BT-05");
        assertExpectation(trip5.getStatus() == TripStatus.CANCELLED, "Тест 6. Отмена из APPROVED разрешена");

        boolean cancelCompleted = false;
        try {
            service.cancelTrip("BT-01");
        } catch (IllegalStateException e) {
            cancelCompleted = true;
        }
        assertExpectation(cancelCompleted && trip1.getStatus() == TripStatus.COMPLETED, "Тест 7. Отмена COMPLETED запрещена");

        // 8. Проверка нормализации id при создании дубликата
        boolean duplicateNormalized = false;
        try {
            service.create(" BT-01 ", emp, LocalDate.of(2025, 5, 1), LocalDate.of(2025, 5, 2));
        } catch (IllegalArgumentException e) {
            duplicateNormalized = true;
        }
        assertExpectation(duplicateNormalized, "Тест 8. Дубликат с пробелами нормализован и отклонён");

        List<BusinessTrip> approvedTrips = service.findByStatus(TripStatus.APPROVED);
        System.out.println("Количество APPROVED заявок: " + approvedTrips.size());
        System.out.println("=== Все тесты успешно пройдены! ===");
    }

    private static void assertExpectation(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError("Проверка провалена: " + message);
        }
        System.out.println("[OK] " + message);
    }
}
