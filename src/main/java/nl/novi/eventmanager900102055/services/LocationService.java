package nl.novi.eventmanager900102055.services;

import nl.novi.eventmanager900102055.dtos.EventDto;
import nl.novi.eventmanager900102055.dtos.LocationDto;
import nl.novi.eventmanager900102055.exceptions.NameDuplicateException;
import nl.novi.eventmanager900102055.exceptions.ResourceNotFoundException;
import nl.novi.eventmanager900102055.models.Location;
import nl.novi.eventmanager900102055.models.User;
import nl.novi.eventmanager900102055.repositories.LocationRepository;
import nl.novi.eventmanager900102055.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LocationService {
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;

    public LocationService(LocationRepository locationRepository, UserRepository userRepository) {
        this.locationRepository = locationRepository;
        this.userRepository = userRepository;
    }

    public LocationDto createLocation(LocationDto locationDto, String username) throws NameDuplicateException, ResourceNotFoundException {
        User user = userRepository.findById(username).orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        Iterable<Location> locations = locationRepository.findAll();
        for (Location l : locations) {
            if (l.getName().equals(locationDto.getName())) {
                throw new NameDuplicateException("This Location already exists");
            }
        }
        Location location = transferLocationDtoToLocation(locationDto);

        location.setUser(user);
        user.getLocationList().add(location);

        userRepository.save(user);
        location = locationRepository.save(location);
        return transferLocationToLocationDto(location);
    }
    public List<LocationDto> findAllLocations() {
        List<Location> locationList = locationRepository.findAll();
        return transferLocationListToLocationDtoList(locationList);
    }

    public LocationDto findLocationById(Long id) {
        Location Location = locationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Location not found"));
        return transferLocationToLocationDto(Location);
    }

    public LocationDto findLocationByName(String name) throws ResourceNotFoundException {
        Location location  = locationRepository.findByName(name);
        if (location == null) {
            throw new ResourceNotFoundException("Location not found");
        }
        return transferLocationToLocationDto(location);
    }

    public LocationDto updateLocation(Long id, LocationDto locationDto) {
        Location location = locationRepository.findById(id).orElse(null);
        if (location == null) {
            return null;
        }
        location.setName(locationDto.getName());
        location.setAddress(locationDto.getAddress());
        location.setEmail(locationDto.getEmail());
        location.setNumberOfSeats(locationDto.getNumberOfSeats());

        return transferLocationToLocationDto(locationRepository.save(location));
    }

    public boolean deleteLocation(Long id) {
        if (locationRepository.existsById(id)) {
            locationRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<LocationDto> transferLocationListToLocationDtoList(List<Location> locationList) {
        List<LocationDto> locationDtoList = new ArrayList<>();

        for (Location location : locationList) {
            locationDtoList.add(transferLocationToLocationDto(location));
        }
        return locationDtoList;
    }

    public Location transferLocationDtoToLocation(LocationDto locationDto) {

        Location location = new Location();
        location.setId(locationDto.getId());
        location.setName(locationDto.getName());
        location.setAddress(locationDto.getAddress());
        location.setEmail(locationDto.getEmail());
        location.setNumberOfSeats(locationDto.getNumberOfSeats());

        return location;
    }

    public LocationDto transferLocationToLocationDto(Location location) {
        LocationDto locationDto = new LocationDto();

        locationDto.setId(location.getId());
        locationDto.setName(location.getName());
        locationDto.setAddress(location.getAddress());
        locationDto.setEmail(location.getEmail());
        locationDto.setNumberOfSeats(location.getNumberOfSeats());

        return locationDto;
    }
}
