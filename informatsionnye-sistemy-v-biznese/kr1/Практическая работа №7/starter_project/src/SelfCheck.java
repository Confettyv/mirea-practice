import java.util.HashMap;
import java.util.Map;

public class SelfCheck {
    public static void main(String[] args) {
        System.out.println("=== RUNNING SELFCHECK FOR VARIANT 1 ===");
        ProductRegistry.clear();

        Employee emp = new Employee("EMP-01", "Иванов Иван Иванович");
        Product laptop = new Product("PRD-01", "Ноутбук ThinkPad", 85000.0);
        Product mouse = new Product("PRD-02", "Беспроводная мышь", 2500.0);
        Product monitor = new Product("PRD-03", "Монитор 27 дюймов", 32000.0);

        // Проверка 1: Контроль уникальности товара
        boolean duplicateProductCaught = false;
        try {
            new Product("PRD-01", "Дубликат ноутбука", 90000.0);
        } catch (IllegalArgumentException e) {
            duplicateProductCaught = true;
            System.out.println("Test 1 (Duplicate Product ID caught): " + e.getMessage());
        }

        OrderService service = new OrderService();
        Order order = service.createOrder(emp);

        // Проверка 2: Парсинг корректных и malformed-строк
        Map<String, Product> catalog = new HashMap<>();
        catalog.put(laptop.getId(), laptop);
        catalog.put(mouse.getId(), mouse);
        catalog.put(monitor.getId(), monitor);

        service.parseAndAddItem(order, "PRD-01,Ноутбук ThinkPad,85000.0,2", catalog);
        service.parseAndAddItem(order, "PRD-02,Беспроводная мышь,2500.0,5", catalog);

        boolean malformedCaught = false;
        try {
            service.parseAndAddItem(order, "PRD-02,Беспроводная мышь,2500.0,НЕ_ЧИСЛО", catalog);
        } catch (IllegalArgumentException e) {
            malformedCaught = true;
            System.out.println("Test 2 (Malformed input caught): " + e.getMessage());
        }

        // Проверка 3: Объединение повторного товара
        order.addProduct(mouse, 3); // 5 + 3 = 8
        System.out.println("Test 3 (Merged duplicates): items = " + order.getItems().size() + ", mouse qty = " + order.getItems().get(1).getQuantity());

        // Проверка 4: Превышение верхнего лимита (Вариант 1: max 10)
        boolean limitCaught = false;
        try {
            order.addProduct(mouse, 3); // 8 + 3 = 11 > 10
        } catch (IllegalStateException e) {
            limitCaught = true;
            System.out.println("Test 4 (Exceed limit 10 caught): " + e.getMessage());
        }

        // Проверка 5: Удаление позиции
        order.addProduct(monitor, 1);
        boolean removed = order.removeProduct("PRD-03");
        System.out.println("Test 5 (Remove monitor): " + removed + ", total = " + order.total());

        // Проверка 6: Подтверждение и неизменяемость
        service.confirmOrder(order);
        System.out.println("Test 6 (Status after confirm): " + order.getStatus());

        boolean modifyBlocked = false;
        try {
            order.addProduct(laptop, 1);
        } catch (IllegalStateException e) {
            modifyBlocked = true;
            System.out.println("Test 7 (Add post-confirm blocked): " + e.getMessage());
        }

        // Проверка 7: Глубокая защита инкапсуляции getItems()
        order.getItems().get(0).increase(1); // Изменяет только копию!
        boolean totalUnchanged = (order.total() == 190000.0) && (order.getItems().get(0).getQuantity() == 2);
        System.out.println("Test 8 (Deep encapsulation safe): total unchanged = " + totalUnchanged);

        if (duplicateProductCaught && malformedCaught && limitCaught && modifyBlocked && totalUnchanged) {
            System.out.println("=== ALL CHECKS PASSED SUCCESSFULLY ===");
        } else {
            System.out.println("=== SELFCHECK FAILED ===");
        }
    }
}
