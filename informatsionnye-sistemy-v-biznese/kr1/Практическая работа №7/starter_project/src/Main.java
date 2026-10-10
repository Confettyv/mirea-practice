import model.Employee;
import model.Order;
import model.Product;
import service.OrderService;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Запуск демонстрации и граничных проверок (Вариант 1) ===");
        OrderService service = new OrderService();
        Employee emp = new Employee("EMP-101", "Иванов Иван Иванович");
        Order order = service.createOrder(emp);

        Product laptop = new Product("PRD-01", "Ноутбук бизнес-серии", 85000.0);
        Product mouse = new Product("PRD-02", "Беспроводная мышь", 2500.0);
        Product monitor = new Product("PRD-03", "Монитор 27 дюймов", 32000.0);

        // 1. Корректное добавление позиций
        order.addProduct(laptop, 1);
        order.addProduct(mouse, 3);
        System.out.printf("Добавлены товары. Позиций: %d, Итого: %.2f руб.%n", order.getItems().size(), order.total());

        // 2. Объединение повторного товара
        order.addProduct(mouse, 4); // стало 3 + 4 = 7 шт (лимит 10 не превышен)
        System.out.printf("После объединения мышей: %d шт, позиций: %d, Итого: %.2f руб.%n", 
                order.getItems().get(1).getQuantity(), order.getItems().size(), order.total());

        // 3. Граничная проверка: добавление товара до верхней границы (10 шт)
        order.addProduct(monitor, 10);
        System.out.printf("Добавлен монитор в количестве 10 шт. Итого: %.2f руб.%n", order.total());

        // 4. Удаление позиции
        boolean removed = order.removeProduct("PRD-01");
        System.out.printf("Удаление ноутбука: %b. Позиций осталось: %d, Итого: %.2f руб.%n", 
                removed, order.getItems().size(), order.total());

        // 5. Подтверждение заказа
        service.confirmOrder(order);
        System.out.printf("Заказ подтвержден. Статус: %s%n", order.getStatus());

        // 6. Проверка защиты от модификации подтвержденного заказа
        try {
            order.addProduct(laptop, 1);
            System.out.println("ОШИБКА: Заказ позволил добавление после confirm!");
        } catch (IllegalStateException e) {
            System.out.println("УСПЕХ: Защита confirm сработала: " + e.getMessage());
        }

        // 7. Проверка лимита варианта 1 (> 10 шт)
        try {
            Order testOrder = service.createOrder(emp);
            testOrder.addProduct(laptop, 11);
            System.out.println("ОШИБКА: Превышение лимита 10 не отловлено!");
        } catch (IllegalArgumentException e) {
            System.out.println("УСПЕХ: Защита лимита Варианта 1 (>10) сработала: " + e.getMessage());
        }

        // 8. Проверка нижней границы (< 1 шт)
        try {
            Order testOrder = service.createOrder(emp);
            testOrder.addProduct(laptop, 0);
            System.out.println("ОШИБКА: Нулевое количество не отловлено!");
        } catch (IllegalArgumentException e) {
            System.out.println("УСПЕХ: Защита нижней границы (0) сработала: " + e.getMessage());
        }

        System.out.println("=== Все проверки успешно завершены ===");
    }
}
