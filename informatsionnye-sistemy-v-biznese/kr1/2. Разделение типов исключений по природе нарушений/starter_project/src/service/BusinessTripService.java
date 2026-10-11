package service;

import model.BusinessTrip;
import model.Employee;
import model.TripStatus;

import java.time.LocalDate;
import java.util.ArrayList;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BusinessTripService {
    private final Map<String, BusinessTrip> trips = new LinkedHashMap<>();

    public BusinessTrip create(String id, Employee employee, LocalDate start, LocalDate end) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Идентификатор командировки обязателен");
        }
        String normalizedId = id.trim();
        if (trips.containsKey(normalizedId)) {
            throw new IllegalArgumentException("Заявка с таким id уже существует: " + normalizedId);
        }
        BusinessTrip trip = new BusinessTrip(normalizedId, employee, start, end);
        trips.put(normalizedId, trip);
        return trip;
    }

    public BusinessTrip findById(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Идентификатор не может быть null");
        }
        BusinessTrip trip = trips.get(id.trim());
        if (trip == null) {
            throw new IllegalArgumentException("Заявка не найдена: " + id.trim());
        }
        return trip;
    }

    public List<BusinessTrip> findByStatus(TripStatus status) {
        List<BusinessTrip> result = new ArrayList<>();
        for (BusinessTrip trip : trips.values()) {
            if (trip.getStatus() == status) {
                result.add(trip);
            }
        }
        return Collections.unmodifiableList(result);
    }

    public void submitTrip(String id) {
        findById(id).submit();
    }

    public void approveTrip(String id) {
        findById(id).approve();
    }

    public void rejectTrip(String id, String reason) {
        findById(id).reject(reason);
    }

    public void cancelTrip(String id) {
        findById(id).cancel();
    }

    public void completeTrip(String id, LocalDate completionDate) {
        findById(id).complete(completionDate);
    }
}
