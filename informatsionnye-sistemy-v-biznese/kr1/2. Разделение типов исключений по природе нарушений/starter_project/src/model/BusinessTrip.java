package model;

import java.time.LocalDate;

public class BusinessTrip {
    public static final int MIN_REJECTION_REASON_LENGTH = 10;

    private final String id;
    private final Employee employee;
    private final LocalDate start;
    private final LocalDate end;
    private TripStatus status;
    private String rejectionReason;

    public BusinessTrip(String id, Employee employee, LocalDate start, LocalDate end) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Идентификатор командировки обязателен");
        }
        if (employee == null) {
            throw new IllegalArgumentException("Сотрудник должен быть указан");
        }
        if (start == null || end == null) {
            throw new IllegalArgumentException("Даты командировки обязательны");
        }
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("Дата окончания не может быть раньше даты начала");
        }
        this.id = id.trim();
        this.employee = employee;
        this.start = start;
        this.end = end;
        this.status = TripStatus.DRAFT;
        this.rejectionReason = null;
    }

    public String getId() {
        return id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public LocalDate getStart() {
        return start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public TripStatus getStatus() {
        return status;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void submit() {
        if (this.status != TripStatus.DRAFT) {
            throw new IllegalStateException("Отправка на согласование возможна только из статуса DRAFT");
        }
        this.status = TripStatus.SUBMITTED;
    }

    public void approve() {
        if (this.status != TripStatus.SUBMITTED) {
            throw new IllegalStateException("Согласование возможно только из статуса SUBMITTED");
        }
        this.status = TripStatus.APPROVED;
    }

    public void reject(String reason) {
        if (this.status != TripStatus.SUBMITTED) {
            throw new IllegalStateException("Отклонение возможно только из статуса SUBMITTED");
        }
        if (reason == null || reason.trim().length() < MIN_REJECTION_REASON_LENGTH) {
            throw new IllegalArgumentException("Причина отклонения должна содержать минимум 10 символов");
        }
        this.rejectionReason = reason.trim();
        this.status = TripStatus.REJECTED;
    }

    public void cancel() {
        if (this.status == TripStatus.COMPLETED || this.status == TripStatus.CANCELLED || this.status == TripStatus.REJECTED) {
            throw new IllegalStateException("Отмена невозможна для статусов COMPLETED, CANCELLED или REJECTED");
        }
        this.status = TripStatus.CANCELLED;
    }

    public void complete(LocalDate completionDate) {
        if (this.status != TripStatus.APPROVED) {
            throw new IllegalStateException("Завершение возможно только из статуса APPROVED");
        }
        if (completionDate == null || completionDate.isBefore(this.end)) {
            throw new IllegalArgumentException("Завершение возможно только в дату окончания или позже");
        }
        this.status = TripStatus.COMPLETED;
    }
}
