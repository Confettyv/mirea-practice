package service;

import model.Employee;
import model.Order;
import model.OrderStatus;

public class OrderService {
    public Order createOrder(Employee employee) {
        return new Order(employee);
    }

    public void validateForConfirm(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Заказ не может быть null");
        }
        if (order.getStatus() == OrderStatus.CONFIRMED) {
            throw new IllegalStateException("Заказ уже подтвержден");
        }
        if (order.getItems().isEmpty()) {
            throw new IllegalStateException("Заказ должен содержать минимум одну позицию перед подтверждением");
        }
    }

    public void confirmOrder(Order order) {
        validateForConfirm(order);
        order.confirm();
    }
}
