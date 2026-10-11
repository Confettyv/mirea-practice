package model;

public class Employee {
    private final String id;
    private final String fullName;
    private final String department;

    public Employee(String id, String fullName, String department) {
        if (id == null || id.isBlank() || fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("Параметры сотрудника не могут быть пустыми");
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
}
