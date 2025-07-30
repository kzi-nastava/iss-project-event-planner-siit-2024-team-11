package org.example.eventy.events.services;

import org.example.eventy.events.models.Location;
import org.example.eventy.events.repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {
    @Autowired
    private LocationRepository locationRepository;

    public Location saveLocation(Location location) {
        try {
            return locationRepository.save(location);
        }
        catch (Exception e) {
            return null;
        }
    }

    public Location getLocation(Long id) {
        return locationRepository.findById(id).orElse(null);
    }

    public List<Location> getLocations() {
        return locationRepository.findAll();
    }
}
