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
        if (id == null || id.isBlank()) throw new IllegalArgumentException("ID cannot be empty");
        if (employee == null) throw new IllegalArgumentException("Employee cannot be null");
        if (start == null || end == null) throw new IllegalArgumentException("Dates cannot be null");
        if (start.isAfter(end)) throw new IllegalArgumentException("Start date cannot be after end date");

        this.id = id;
        this.employee = employee;
        this.start = start;
        this.end = end;
        this.status = TripStatus.DRAFT;
        this.rejectionReason = null;
    }

    public String getId() { return id; }
    public Employee getEmployee() { return employee; }
    public LocalDate getStart() { return start; }
    public LocalDate getEnd() { return end; }
    public TripStatus getStatus() { return status; }
    public String getRejectionReason() { return rejectionReason; }

    public void submit() {
        if (this.status != TripStatus.DRAFT) {
            throw new IllegalStateException("Submission is allowed only from DRAFT status. Current: " + this.status);
        }
        if (this.employee == null || this.start == null || this.end == null) {
            throw new IllegalArgumentException("Required fields must be populated");
        }
        this.status = TripStatus.SUBMITTED;
    }

    public void approve() {
        if (this.status != TripStatus.SUBMITTED) {
            throw new IllegalStateException("Approval is allowed only from SUBMITTED status. Current: " + this.status);
        }
        this.status = TripStatus.APPROVED;
    }

    public void reject(String reason) {
        if (this.status != TripStatus.SUBMITTED) {
            throw new IllegalStateException("Rejection is allowed only from SUBMITTED status. Current: " + this.status);
        }
        if (reason == null || reason.trim().length() < MIN_REJECTION_REASON_LENGTH) {
            throw new IllegalArgumentException("Rejection reason must be at least " + MIN_REJECTION_REASON_LENGTH + " characters");
        }
        this.rejectionReason = reason.trim();
        this.status = TripStatus.REJECTED;
    }

    public void cancel() {
        if (this.status == TripStatus.COMPLETED || this.status == TripStatus.CANCELLED || this.status == TripStatus.REJECTED) {
            throw new IllegalStateException("Cancellation is not allowed from status: " + this.status);
        }
        this.status = TripStatus.CANCELLED;
    }

    public void complete(LocalDate completionDate) {
        if (this.status != TripStatus.APPROVED) {
            throw new IllegalStateException("Completion is allowed only from APPROVED status. Current: " + this.status);
        }
        if (completionDate == null) {
            throw new IllegalArgumentException("Completion date cannot be null");
        }
        if (completionDate.isBefore(this.end)) {
            throw new IllegalArgumentException("Completion date cannot be earlier than trip end date: " + this.end);
        }
        this.status = TripStatus.COMPLETED;
    }

    @Override
    public String toString() {
        return "BusinessTrip{" + id + ", emp=" + employee.getFullName() + ", status=" + status + ", period=" + start + ".." + end + (rejectionReason != null ? ", reason='" + rejectionReason + "'" : "") + "}";
    }
}
