package nl.novi.eventmanager900102055.controllers;

import nl.novi.eventmanager900102055.services.LocationService;

public class LocationController {
    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }
}
