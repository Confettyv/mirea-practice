package model;

import java.time.LocalDate;
import java.util.Objects;

public class BusinessTrip {
    public static final int MIN_REJECTION_REASON_LENGTH = 10; // Вариант 1

    private final String id;
    private final Employee employee;
    private final LocalDate start;
    private final LocalDate end;
    private TripStatus status;
    private String rejectionReason;

    public BusinessTrip(String id, Employee employee, LocalDate start, LocalDate end) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Trip id cannot be empty");
        }
        if (employee == null) {
            throw new IllegalArgumentException("Employee must be specified");
        }
        if (start == null || end == null) {
            throw new IllegalArgumentException("Dates must not be null");
        }
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
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
            throw new IllegalStateException("Submit is allowed only from DRAFT, current: " + this.status);
        }
        if (this.employee == null || this.start == null || this.end == null) {
            throw new IllegalArgumentException("Cannot submit trip with incomplete fields");
        }
        this.status = TripStatus.SUBMITTED;
    }

    public void approve() {
        if (this.status != TripStatus.SUBMITTED) {
            throw new IllegalStateException("Approve is allowed only from SUBMITTED, current: " + this.status);
        }
        this.status = TripStatus.APPROVED;
    }

    public void reject(String reason) {
        if (this.status != TripStatus.SUBMITTED) {
            throw new IllegalStateException("Reject is allowed only from SUBMITTED, current: " + this.status);
        }
        if (reason == null || reason.trim().length() < MIN_REJECTION_REASON_LENGTH) {
            throw new IllegalArgumentException("Rejection reason must contain at least " 
                    + MIN_REJECTION_REASON_LENGTH + " characters");
        }
        this.rejectionReason = reason.trim();
        this.status = TripStatus.REJECTED;
    }

    public void cancel() {
        if (this.status == TripStatus.COMPLETED) {
            throw new IllegalStateException("Cannot cancel completed trip");
        }
        if (this.status == TripStatus.CANCELLED || this.status == TripStatus.REJECTED) {
            throw new IllegalStateException("Trip is already in terminal state: " + this.status);
        }
        this.status = TripStatus.CANCELLED;
    }

    public void complete(LocalDate currentDate) {
        if (this.status != TripStatus.APPROVED) {
            throw new IllegalStateException("Complete is allowed only from APPROVED, current: " + this.status);
        }
        if (currentDate == null) {
            throw new IllegalArgumentException("Current date cannot be null");
        }
        if (!currentDate.isAfter(this.end)) {
            throw new IllegalStateException("Cannot complete trip before or on end date. Completion date must be strictly after end date: " + this.end);
        }
        this.status = TripStatus.COMPLETED;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BusinessTrip that = (BusinessTrip) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "BusinessTrip{id='" + id + "', status=" + status + ", employee=" + employee.getFullName()
                + ", period=[" + start + " .. " + end + "]}";
    }
}
