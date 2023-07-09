package nl.novi.eventmanager900102055.controllers;

import nl.novi.eventmanager900102055.services.UserService;

public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
}
