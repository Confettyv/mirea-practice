package com.example.order;

import com.example.order.model.Employee;
import com.example.order.model.Order;
import com.example.order.model.OrderItem;
import com.example.order.model.OrderStatus;
import com.example.order.model.Product;

public class SelfCheck {
    public static void runChecks() {
        System.out.println("[SelfCheck] Запуск тестов регрессии и граничных условий...");
        
        // 1. Проверка инвариантов Product (корректное типовое значение)
        Product p1 = new Product("P001", "Keyboard", 50.0);
        if (!p1.getId().equals("P001") || p1.getPrice() != 50.0) {
            throw new IllegalStateException("Product invariants check failed for valid data.");
        }

        // 2. Проверка граничных условий количества (нижняя граница: 1, верхняя граница: 10)
        Employee emp = new Employee("E001", "Иванов И.И.");
        Order orderQuantityBounds = new Order(emp);
        Product pTest = new Product("PTEST", "Test Item", 10.0);
        
        // Нижняя граница: 1 (успешно)
        orderQuantityBounds.addProduct(pTest, 1);
        if (orderQuantityBounds.getItems().get(0).getQuantity() != 1) {
            throw new IllegalStateException("Lower bound quantity (1) check failed.");
        }
        
        // Верхняя граница: 10 (успешно)
        orderQuantityBounds = new Order(emp); // Reset order
        orderQuantityBounds.addProduct(pTest, 10);
        if (orderQuantityBounds.getItems().get(0).getQuantity() != 10) {
            throw new IllegalStateException("Upper bound quantity (10) check failed.");
        }

        // 3. Проверка выхода за границы количества (контролируемая ошибка)
        boolean lowerBoundExceeded = false;
        try {
            new OrderItem(pTest, 0); // Quantity below MIN_QUANTITY
        } catch (IllegalArgumentException e) {
            lowerBoundExceeded = true;
        }
        if (!lowerBoundExceeded) {
            throw new IllegalStateException("Quantity 0 was not rejected.");
        }

        boolean upperBoundExceeded = false;
        try {
            new OrderItem(pTest, 11); // Quantity above MAX_QUANTITY
        } catch (IllegalArgumentException e) {
            upperBoundExceeded = true;
        }
        if (!upperBoundExceeded) {
            throw new IllegalStateException("Quantity 11 was not rejected.");
        }

        // 4. Проверка пустого или отсутствующего значения (контролируемая ошибка)
        boolean nullProductIdRejected = false;
        try {
            new Product(null, "Name", 10.0);
        } catch (IllegalArgumentException e) {
            nullProductIdRejected = true;
        }
        if (!nullProductIdRejected) {
            throw new IllegalStateException("Null Product ID was not rejected.");
        }

        boolean emptyProductNameRejected = false;
        try {
            new Product("P00X", "", 10.0);
        } catch (IllegalArgumentException e) {
            emptyProductNameRejected = true;
        }
        if (!emptyProductNameRejected) {
            throw new IllegalStateException("Empty Product Name was not rejected.");
        }

        // 5. Проверка значения неверного формата (контролируемая ошибка)
        boolean negativePriceRejected = false;
        try {
            new Product("P00Y", "Product Y", -5.0);
        } catch (IllegalArgumentException e) {
            negativePriceRejected = true;
        }
        if (!negativePriceRejected) {
            throw new IllegalStateException("Negative Product Price was not rejected.");
        }

        boolean zeroPriceRejected = false;
        try {
            new Product("P00Z", "Product Z", 0.0);
        } catch (IllegalArgumentException e) {
            zeroPriceRejected = true;
        }
        if (!zeroPriceRejected) {
            throw new IllegalStateException("Zero Product Price was not rejected.");
        }

        // 6. Проверка объединения позиций (повтор данных)
        Product p2 = new Product("P002", "Mouse", 25.0);
        Order orderMerge = new Order(emp);
        orderMerge.addProduct(p1, 2);
        orderMerge.addProduct(p2, 1);
        orderMerge.addProduct(p1, 3); // Merge P001
        if (orderMerge.getItems().size() != 2 || orderMerge.getItems().stream().filter(item -> item.getProduct().getId().equals("P001")).findFirst().get().getQuantity() != 5 || orderMerge.total() != (5 * 50.0 + 1 * 25.0)) {
            throw new IllegalStateException("Order merge/total check failed.");
        }

        // 7. Проверка удаления позиции
        orderMerge.removeProduct("P002");
        if (orderMerge.getItems().size() != 1) {
            throw new IllegalStateException("Order position removal check failed.");
        }
        boolean removeNonExisting = false;
        try {
            orderMerge.removeProduct("P999");
        } catch (IllegalArgumentException e) {
            removeNonExisting = true;
        }
        if (!removeNonExisting) {
            throw new IllegalStateException("Removing non-existing product was not rejected.");
        }

        // 8. Проверка неизменяемости после confirm
        orderMerge.confirm();
        if (orderMerge.getStatus() != OrderStatus.CONFIRMED) {
            throw new IllegalStateException("Order status check failed.");
        }
        boolean addBlocked = false;
        try {
            orderMerge.addProduct(p2, 1);
        } catch (IllegalStateException e) {
            addBlocked = true;
        }
        if (!addBlocked) {
            throw new IllegalStateException("Confirmed order modification (add) was not blocked.");
        }
        boolean removeBlocked = false;
        try {
            orderMerge.removeProduct("P001");
        } catch (IllegalStateException e) {
            removeBlocked = true;
        }
        if (!removeBlocked) {
            throw new IllegalStateException("Confirmed order modification (remove) was not blocked.");
        }

        // 9. Проверка защиты глубокой копией getItems()
        OrderItem externalItem = orderMerge.getItems().get(0);
        try {
            externalItem.increase(1); // Attempt to modify external copy
        } catch (Exception ignored) { /* Expected to fail if increase is called on a copy, or if it exceeds max quantity */ }
        if (orderMerge.getItems().get(0).getQuantity() != 5) { // Check original order's quantity
            throw new IllegalStateException("Internal state modified via external getItems().");
        }

        System.out.println("[SelfCheck] Все 9 проверок пройдены успешно!");
    }
}
