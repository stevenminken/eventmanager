package nl.novi.eventmanager900102055.controllers;

import jakarta.validation.Valid;
import nl.novi.eventmanager900102055.dtos.LocationDto;
import nl.novi.eventmanager900102055.exceptions.NameDuplicateException;
import nl.novi.eventmanager900102055.exceptions.ResourceNotFoundException;
import nl.novi.eventmanager900102055.services.LocationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/locations")
public class LocationController {
    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PostMapping("/create_location")
    public ResponseEntity<Object> createLocation(@Valid @RequestBody LocationDto locationDto, BindingResult bindingResult) throws NameDuplicateException, ResourceNotFoundException {

        if (bindingResult.hasFieldErrors()) {
            StringBuilder sb = new StringBuilder();
            for (FieldError fe : bindingResult.getFieldErrors()) {
                sb.append(fe.getField()).append(": ");
                sb.append(fe.getDefaultMessage());
                sb.append("\n");
            }
            return ResponseEntity.badRequest().body(sb.toString());
        } else {
            LocationDto dto = locationService.createLocation(locationDto);
            URI location = UriComponentsBuilder
                    .fromPath("/locations/{lastName}")
                    .buildAndExpand(dto.getName())
                    .toUri();

            return ResponseEntity.created(location).body("location created: " + dto.getName() + " with id: " + +dto.getId());
        }
    }

    @GetMapping(value = "/find_all_locations")
    public ResponseEntity<List<LocationDto>> findAllLocations() {
        List<LocationDto> locationDtoList;
        locationDtoList = locationService.findAllLocations();
        return ResponseEntity.ok().body(locationDtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findLocationById(@PathVariable("id") Long id) {
        try {
            LocationDto locationDto = locationService.findLocationById(id);
            return ResponseEntity.ok().body(locationDto);
        } catch (Exception e) {
            String errorMessage = "Error occurred while fetching the location: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }

    @PostMapping(value = "/find_location_by_name")
    public ResponseEntity<Object> findLocationByName(@RequestBody Map<String, Object> requestBody) {
        try {
            LocationDto locationDto = locationService.findLocationByName(requestBody.get("name").toString());
            return ResponseEntity.ok().body(locationDto);
        } catch (Exception e) {
            String errorMessage = "Error occurred while fetching the Location: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateLocation(@PathVariable("id") Long id, @Valid @RequestBody LocationDto locationDto, BindingResult br) {
        if (br.hasFieldErrors()) {
            StringBuilder sb = new StringBuilder();
            for (FieldError fe : br.getFieldErrors()) {
                sb.append(fe.getField()).append(": ");
                sb.append(fe.getDefaultMessage());
                sb.append("/n");
            }
            return ResponseEntity.badRequest().body(sb.toString());
        } else {
            LocationDto updatedlocationDto = locationService.updateLocation(id, locationDto);
            return ResponseEntity.ok().body(updatedlocationDto);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteLocation(@PathVariable("id") Long id) {
        boolean deleted = locationService.deleteLocation(id);
        if (deleted) {
            return ResponseEntity.ok().body("location deleted");
        } else {
            return ResponseEntity.badRequest().body("location not deleted");
        }
    }

}
