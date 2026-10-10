package model;

public class Employee {
    private final String id;
    private final String fullName;
    private final String department;

    public Employee(String id, String fullName, String department) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("Employee id cannot be empty");
        if (fullName == null || fullName.isBlank()) throw new IllegalArgumentException("Employee fullName cannot be empty");
        this.id = id;
        this.fullName = fullName;
        this.department = department == null ? "" : department;
    }

    public String getId() { return id; }
    public String getFullName() { return fullName; }
    public String getDepartment() { return department; }

    @Override
    public String toString() {
        return fullName + " (" + department + ")";
    }
}
