package nl.novi.eventmanager900102055.controllers;

import nl.novi.eventmanager900102055.dtos.UserDto;
import nl.novi.eventmanager900102055.exceptions.BadRequestException;
import nl.novi.eventmanager900102055.exceptions.NameDuplicateException;
import nl.novi.eventmanager900102055.exceptions.ResourceNotFoundException;
import nl.novi.eventmanager900102055.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> findAllUsers() {

        List<UserDto> UserDtoList = userService.findAllUsers();
        return ResponseEntity.ok().body(UserDtoList);
    }

    @GetMapping(value = "/{username}")
    public ResponseEntity<UserDto> findUserByName(@PathVariable("username") String username) {

        UserDto optionalUser = userService.findUserByUsername(username);
        return ResponseEntity.ok().body(optionalUser);
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto dto) throws NameDuplicateException, ResourceNotFoundException {

        String newUsername = userService.createUser(dto);
        userService.addAuthority(newUsername, "ROLE_USER");

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{username}")
                .buildAndExpand(newUsername).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping(value = "/{username}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("username") String username, @RequestBody UserDto dto) throws ResourceNotFoundException {

        userService.updateUser(username, dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/{username}")
    public ResponseEntity<Object> deleteUser(@PathVariable("username") String username) {
        userService.deleteUser(username);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/{username}/authorities")
    public ResponseEntity<Object> addUserAuthority(@PathVariable("username") String username, @RequestBody Map<String, Object> fields) {
        try {
            String authorityName = (String) fields.get("authority");
            userService.addAuthority(username, authorityName);
            return ResponseEntity.noContent().build();
        }
        catch (Exception ex) {
            throw new BadRequestException();
        }
    }

    @DeleteMapping(value = "/{username}/authorities/{authority}")
    public ResponseEntity<Object> deleteUserAuthority(@PathVariable("username") String username, @PathVariable("authority") String authority) throws ResourceNotFoundException {
        userService.removeAuthority(username, authority);
        return ResponseEntity.noContent().build();
    }

}
