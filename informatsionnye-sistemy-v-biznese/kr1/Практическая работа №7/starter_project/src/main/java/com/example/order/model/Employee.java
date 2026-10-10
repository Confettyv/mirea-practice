package com.example.order.model;

public class Employee {
    private final String id;
    private final String fullName;

    public Employee(String id, String fullName) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Employee ID cannot be null or empty.");
        }
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Employee full name cannot be null or empty.");
        }
        this.id = id.trim();
        this.fullName = fullName.trim();
    }

    public String getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    @Override
    public String toString() {
        return "Employee{id='" + id + "', fullName='" + fullName + "'}";
    }
}
