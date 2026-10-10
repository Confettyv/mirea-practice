package service;

import model.BusinessTrip;
import model.Employee;
import model.TripStatus;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class BusinessTripService {
    private final Map<String, BusinessTrip> trips = new LinkedHashMap<>();

    public BusinessTrip create(String id, Employee employee, LocalDate start, LocalDate end) {
        if (trips.containsKey(id)) {
            throw new IllegalArgumentException("Trip with id " + id + " already exists");
        }
        BusinessTrip trip = new BusinessTrip(id, employee, start, end);
        trips.put(id, trip);
        return trip;
    }

    public BusinessTrip findById(String id) {
        return trips.get(id);
    }

    public List<BusinessTrip> findByStatus(TripStatus status) {
        return trips.values().stream()
                .filter(trip -> trip.getStatus() == status)
                .collect(Collectors.toList());
    }

    public List<BusinessTrip> getAllTrips() {
        return new ArrayList<>(trips.values());
    }
}
