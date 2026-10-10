package service;

import model.BusinessTrip;
import model.Employee;
import model.TripStatus;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

public class BusinessTripService {
    private final Map<String, BusinessTrip> tripStorage = new LinkedHashMap<>();

    public BusinessTrip create(String id, Employee employee, LocalDate start, LocalDate end) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Trip id cannot be null or blank");
        }
        if (tripStorage.containsKey(id.trim())) {
            throw new IllegalArgumentException("Trip with id " + id + " already exists");
        }
        BusinessTrip trip = new BusinessTrip(id, employee, start, end);
        tripStorage.put(trip.getId(), trip);
        return trip;
    }

    public BusinessTrip createFromStrings(String id, Employee employee, String startDateStr, String endDateStr) {
        if (startDateStr == null || endDateStr == null || startDateStr.isBlank() || endDateStr.isBlank()) {
            throw new IllegalArgumentException("Date strings cannot be null or blank");
        }
        LocalDate start;
        LocalDate end;
        try {
            start = LocalDate.parse(startDateStr.trim());
            end = LocalDate.parse(endDateStr.trim());
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format: " + e.getMessage(), e);
        }
        return create(id, employee, start, end);
    }

    public Optional<BusinessTrip> findById(String id) {
        return Optional.ofNullable(tripStorage.get(id));
    }

    public List<BusinessTrip> findByStatus(TripStatus status) {
        if (status == null) {
            return Collections.emptyList();
        }
        return tripStorage.values().stream()
                .filter(trip -> trip.getStatus() == status)
                .collect(Collectors.toList());
    }

    public List<BusinessTrip> getAllTrips() {
        return new ArrayList<>(tripStorage.values());
    }
}
