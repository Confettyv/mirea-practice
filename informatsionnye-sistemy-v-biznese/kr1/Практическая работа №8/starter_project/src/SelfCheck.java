import model.BusinessTrip;
import model.Employee;
import model.TripStatus;
import service.BusinessTripService;

import java.time.LocalDate;
import java.util.List;

public class SelfCheck {
    public static void main(String[] args) {
        System.out.println("=== Запуск комплексной самопроверки (SelfCheck) ===");
        BusinessTripService service = new BusinessTripService();
        Employee emp1 = new Employee("E-1", "Иванов Иван", "Разработка");
        Employee emp2 = new Employee("E-2", "Петрова Анна", "Маркетинг");

        // Тест 1: Успешный полный цикл DRAFT -> SUBMITTED -> APPROVED -> COMPLETED
        BusinessTrip t1 = service.create("T-1", emp1, LocalDate.of(2025, 5, 1), LocalDate.of(2025, 5, 5));
        assert t1.getStatus() == TripStatus.DRAFT : "Ошибка: исходный статус должен быть DRAFT";
        t1.submit();
        assert t1.getStatus() == TripStatus.SUBMITTED : "Ошибка: статус должен быть SUBMITTED";
        t1.approve();
        assert t1.getStatus() == TripStatus.APPROVED : "Ошибка: статус должен быть APPROVED";
        t1.complete(LocalDate.of(2025, 5, 6));
        assert t1.getStatus() == TripStatus.COMPLETED : "Ошибка: статус должен быть COMPLETED";
        System.out.println("Тест 1 (Полный успешный жизненный цикл): пройден");

        // Тест 2: Отклонение заявки с валидной причиной (>= 10 символов, Вариант 1)
        BusinessTrip t2 = service.create("T-2", emp2, LocalDate.of(2025, 6, 1), LocalDate.of(2025, 6, 10));
        t2.submit();
        t2.reject("Недостаточно средств на командировку");
        assert t2.getStatus() == TripStatus.REJECTED : "Ошибка: статус должен быть REJECTED";
        System.out.println("Тест 2 (Отклонение с валидной причиной): пройден");

        // Тест 3: Граничная проверка отклонения - короткая причина (< 10 символов)
        BusinessTrip t3 = service.create("T-3", emp1, LocalDate.of(2025, 7, 1), LocalDate.of(2025, 7, 3));
        t3.submit();
        boolean caughtArgEx = false;
        try {
            t3.reject("Отказ!");
        } catch (IllegalArgumentException e) {
            caughtArgEx = true;
        }
        assert caughtArgEx : "Ошибка: отклонение с причиной < 10 символов должно бросать IllegalArgumentException";
        assert t3.getStatus() == TripStatus.SUBMITTED : "Ошибка: статус не должен измениться при неудачной операции";
        System.out.println("Тест 3 (Контроль минимальной длины причины): пройден");

        // Тест 4: Недопустимый прямой переход DRAFT -> APPROVED (IllegalStateException)
        BusinessTrip t4 = service.create("T-4", emp2, LocalDate.of(2025, 8, 1), LocalDate.of(2025, 8, 5));
        boolean caughtStateEx = false;
        try {
            t4.approve();
        } catch (IllegalStateException e) {
            caughtStateEx = true;
        }
        assert caughtStateEx : "Ошибка: прямой approve из DRAFT должен вызывать IllegalStateException";
        System.out.println("Тест 4 (Защита инварианта переходов состояний): пройден");

        // Тест 5: Завершение до даты окончания командировки (IllegalArgumentException)
        BusinessTrip t5 = service.create("T-5", emp1, LocalDate.of(2025, 9, 10), LocalDate.of(2025, 9, 20));
        t5.submit();
        t5.approve();
        boolean caughtDateEx = false;
        try {
            t5.complete(LocalDate.of(2025, 9, 15));
        } catch (IllegalArgumentException e) {
            caughtDateEx = true;
        }
        assert caughtDateEx : "Ошибка: закрытие до даты окончания должно бросать IllegalArgumentException";
        System.out.println("Тест 5 (Контроль даты завершения): пройден");

        // Тест 6: Отмена после COMPLETED запрещена
        boolean caughtCancelEx = false;
        try {
            t1.cancel();
        } catch (IllegalStateException e) {
            caughtCancelEx = true;
        }
        assert caughtCancelEx : "Ошибка: отмена COMPLETED заявки должна быть запрещена";
        System.out.println("Тест 6 (Запрет отмены завершённой заявки): пройден");

        // Проверка сервиса выборки по статусу
        List<BusinessTrip> approvedTrips = service.findByStatus(TripStatus.APPROVED);
        assert approvedTrips.size() == 1 && approvedTrips.get(0).getId().equals("T-5");
        System.out.println("Тест 7 (Выборка заявок по статусу в сервисе): пройден");

        System.out.println("=== Все тесты SelfCheck успешно завершены! ===");
    }
}
