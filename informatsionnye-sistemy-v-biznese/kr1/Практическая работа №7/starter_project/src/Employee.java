public class Employee {
    private final String id;
    private final String fullName;

    public Employee(String id, String fullName) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Employee id cannot be empty");
        }
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Employee fullName cannot be empty");
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
}
