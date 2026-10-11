package starter_project.src;

public enum RiskLevel {
    LOW,
    MEDIUM,
    HIGH;

    public static RiskLevel parse(String raw) {
        if (raw == null || raw.trim().isEmpty()) {
            throw new IllegalArgumentException("Отсутствует значение уровня риска");
        }
        try {
            return RiskLevel.valueOf(raw.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Неверный формат уровня риска: " + raw);
        }
    }
}
