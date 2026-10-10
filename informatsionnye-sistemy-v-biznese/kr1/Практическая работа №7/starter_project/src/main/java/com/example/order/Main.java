package com.example.order;

import com.example.order.model.Employee;
import com.example.order.model.Order;
import com.example.order.model.Product;
import com.example.order.service.OrderService;
import com.example.order.util.ProductRepository;

public class Main {
    public static void main(String[] args) {
        System.out.println("## Практическая работа №7");
        System.out.println("Вариант 1\n");

        // Вызов встроенного SelfCheck
        SelfCheck.runChecks();

        System.out.println("\n--- Демонстрация работы приложения ---");
        ProductRepository repo = new ProductRepository();
        OrderService service = new OrderService();

        Product p1 = repo.getProductById("P001"); // Keyboard, 50.00
        Product p2 = repo.getProductById("P002"); // Mouse, 25.00
        Product p3 = repo.getProductById("P003"); // Monitor, 200.00

        Employee employee = new Employee("E001", "Иванов Иван");
        Order order = service.createOrder(employee);

        System.out.println("\n--- Сценарий 1: Добавление и объединение позиций ---");
        order.addProduct(p1, 2); // Добавляем 2 клавиатуры
        order.addProduct(p2, 1); // Добавляем 1 мышь
        order.addProduct(p1, 3); // Добавляем еще 3 клавиатуры (объединение)
        System.out.println(order);

        System.out.println("\n--- Сценарий 2: Удаление позиции ---");
        order.removeProduct("P002"); // Удаляем мышь
        System.out.println("После удаления P002:");
        System.out.println(order);

        System.out.println("\n--- Сценарий 3: Подтверждение заказа ---");
        if (service.validateForConfirm(order)) {
            order.confirm();
            System.out.println("Заказ успешно подтверждён! Статус: " + order.getStatus());
            System.out.println(order);
        }

        System.out.println("\n--- Сценарий 4: Попытка модификации подтверждённого заказа (ошибка) ---");
        try {
            order.addProduct(p3, 1); // Попытка добавить монитор
        } catch (IllegalStateException e) {
            System.out.println("ОШИБКА: " + e.getMessage());
        }
        try {
            order.removeProduct("P001"); // Попытка удалить клавиатуру
        } catch (IllegalStateException e) {
            System.out.println("ОШИБКА: " + e.getMessage());
        }

        System.out.println("\n--- Сценарий 5: Граничные проверки количества (демонстрация) ---");
        Order orderBounds = service.createOrder(new Employee("E002", "Петров Петр"));
        Product pSmall = new Product("PSMALL", "Small Item", 1.0);

        try {
            orderBounds.addProduct(pSmall, OrderItem.MIN_QUANTITY - 1); // Ниже нижней границы
        } catch (IllegalArgumentException e) {
            System.out.println("ОШИБКА (нижняя граница): " + e.getMessage());
        }
        try {
            orderBounds.addProduct(pSmall, OrderItem.MAX_QUANTITY + 1); // Выше верхней границы
        } catch (IllegalArgumentException e) {
            System.out.println("ОШИБКА (верхняя граница): " + e.getMessage());
        }
        orderBounds.addProduct(pSmall, OrderItem.MIN_QUANTITY); // Точно нижняя граница
        orderBounds.addProduct(pSmall, OrderItem.MAX_QUANTITY); // Точно верхняя граница (объединение)
        System.out.println("Заказ с граничными значениями количества:\n" + orderBounds);

        System.out.println("\n--- Сценарий 6: Проверка некорректных входных данных (демонстрация) ---");
        try {
            new Product(null, "Invalid Product", 10.0);
        } catch (IllegalArgumentException e) {
            System.out.println("ОШИБКА (null ID): " + e.getMessage());
        }
        try {
            new Product("P_INV", "", 10.0);
        } catch (IllegalArgumentException e) {
            System.out.println("ОШИБКА (empty name): " + e.getMessage());
        }
        try {
            new Product("P_INV2", "Product Name", -1.0);
        } catch (IllegalArgumentException e) {
            System.out.println("ОШИБКА (negative price): " + e.getMessage());
        }
    }
}
