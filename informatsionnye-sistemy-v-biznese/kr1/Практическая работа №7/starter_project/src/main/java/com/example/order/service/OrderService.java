package com.example.order.service;

import com.example.order.model.Employee;
import com.example.order.model.Order;

public class OrderService {
    public Order createOrder(Employee employee) {
        return new Order(employee);
    }

    public boolean validateForConfirm(Order order) {
        if (order == null) {
            return false;
        }
        return !order.getItems().isEmpty();
    }
}
