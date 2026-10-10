package model;

import java.util.Objects;

public class Employee {
    private final String id;
    private final String fullName;
    private final String department;

    public Employee(String id, String fullName, String department) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Employee id cannot be empty");
        }
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("Employee fullName cannot be empty");
        }
        this.id = id.trim();
        this.fullName = fullName.trim();
        this.department = department != null ? department.trim() : "";
    }

    public String getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getDepartment() {
        return department;
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

    @Override
    public String toString() {
        return fullName + " (" + id + ", " + department + ")";
    }
}
