package nl.novi.eventmanager900102055.controllers;

import nl.novi.eventmanager900102055.dtos.UserDto;
import nl.novi.eventmanager900102055.exceptions.BadRequestException;
import nl.novi.eventmanager900102055.exceptions.NameDuplicateException;
import nl.novi.eventmanager900102055.exceptions.ResourceNotFoundException;
import nl.novi.eventmanager900102055.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody UserDto dto) throws NameDuplicateException, ResourceNotFoundException {

        String newUsername = userService.createUser(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body("User created: " + newUsername);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> findAllUsers() {

        List<UserDto> userDtoList = userService.findAllUsers();
        return ResponseEntity.ok().body(userDtoList);
    }

    @GetMapping(value = "/{username}")
    public ResponseEntity<UserDto> findUserByName(@PathVariable("username") String username) {

        UserDto optionalUser = userService.findUserByUsername(username);
        return ResponseEntity.ok().body(optionalUser);
    }

    @PutMapping
    public ResponseEntity<UserDto> updateUserPassword(@RequestBody UserDto userDto) throws ResourceNotFoundException {

        userService.updateUserPassword(userDto);
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
        } catch (Exception ex) {
            throw new BadRequestException();
        }
    }

    @DeleteMapping(value = "/{username}/authorities/{authority}")
    public ResponseEntity<Object> deleteUserAuthority(@PathVariable("username") String username, @PathVariable("authority") String authority) throws ResourceNotFoundException {
        userService.removeAuthority(username, authority);
        return ResponseEntity.noContent().build();
    }

}
