package model;

import java.util.Objects;

public class Employee {
    private final String id;
    private final String fullName;

    public Employee(String id, String fullName) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Идентификатор сотрудника не может быть пустым");
        }
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("ФИО сотрудника не может быть пустым");
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(id, employee.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
