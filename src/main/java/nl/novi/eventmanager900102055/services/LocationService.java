package nl.novi.eventmanager900102055.services;

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

    public List<LocationDto> findAllLocations() {
        List<Location> locationList = locationRepository.findAll();
        return transferLocationListToLocationDtoList(locationList);
    }

    public LocationDto createLocation(LocationDto locationDto, long userId) throws NameDuplicateException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Iterable<Location> Locations = locationRepository.findAll();
        for (Location l : Locations) {
            if (l.getName().equals(locationDto.getName())) {
                throw new NameDuplicateException("This Location already exists");
            }
        }
        Location location = transferLocationDtoToLocation(locationDto);

        location.setUser(user);
        user.getLocationList().add(location);

        userRepository.save(user);

        return transferLocationToLocationDto(locationRepository.save(location));
    }

    public LocationDto findLocationById(Long id) {
        Location location = locationRepository.findById(id).orElse(null);
        if (location == null) {
            return null;
        }
        return transferLocationToLocationDto(location);
    }

    public List<LocationDto> getLocationByName(String name) throws ResourceNotFoundException {
        Iterable<Location> iterableLocations = locationRepository.findByName(name);
        if (iterableLocations == null) {
            throw new ResourceNotFoundException("Can't find this location");
        }
        ArrayList<Location> locationList = new ArrayList<>();

        for (Location location : iterableLocations) {
            locationList.add(location);
        }

        return transferLocationListToLocationDtoList(locationList);
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
            LocationDto locationDto = new LocationDto();
            locationDto.setId(location.getId());
            locationDto.setName(location.getName());
            locationDto.setAddress(location.getAddress());
            locationDto.setEmail(location.getEmail());
            locationDto.setNumberOfSeats(location.getNumberOfSeats());
            locationDtoList.add(locationDto);
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
