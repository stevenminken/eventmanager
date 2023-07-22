package nl.novi.eventmanager900102055.controllers;

import jakarta.validation.Valid;
import nl.novi.eventmanager900102055.dtos.LocationDto;
import nl.novi.eventmanager900102055.dtos.UserDto;
import nl.novi.eventmanager900102055.exceptions.BadRequestException;
import nl.novi.eventmanager900102055.exceptions.NameDuplicateException;
import nl.novi.eventmanager900102055.exceptions.ResourceNotFoundException;
import nl.novi.eventmanager900102055.models.Authority;
import nl.novi.eventmanager900102055.models.User;
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

    @PostMapping(value = "/create_user")
    public ResponseEntity<String> createUser(@Valid @RequestBody UserDto dto) throws NameDuplicateException, ResourceNotFoundException {

        String username = userService.createUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("User created: " + username);
    }

    @GetMapping(value = "/find_all_users")
    public ResponseEntity<List<UserDto>> findAllUsers() {

        List<UserDto> userDtoList = userService.findAllUsers();
        return ResponseEntity.ok().body(userDtoList);
    }

    @PostMapping(value = "/find_user")
    public ResponseEntity<Object> findUserByName(@RequestBody Map<String, Object> requestBody) {

        try {
            UserDto optionalUser = userService.findUserByUsername(requestBody.get("name").toString());
            return ResponseEntity.ok().body(optionalUser);
        } catch (Exception e) {
            String errorMessage = "Error occurred while fetching the user: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }

    @PutMapping(value = "/update_user")
    public ResponseEntity<Object> updateUserPassword(@RequestBody UserDto userDto) throws ResourceNotFoundException {

        userService.updateUserPassword(userDto);
        return ResponseEntity.ok().body("User updated");
    }

    @DeleteMapping(value = "/delete_user")
    public ResponseEntity<Object> deleteUser(@RequestBody Map<String, Object> requestBody) {
        try {
            String username = requestBody.get("username").toString();
            userService.deleteUser(username);
            return ResponseEntity.ok().body("User deleted");
        } catch (Exception e) {
            String errorMessage = "Error occurred while deleting the user: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }

    @PostMapping(value = "/authority")
    public ResponseEntity<Object> addUserAuthority(@RequestBody Map<String, Object> fields) throws ResourceNotFoundException {

        String authorityName = (String) fields.get("authority");
        String username = (String) fields.get("username");

        UserDto user = userService.findUserByUsername(username);
        if (user != null) {
            boolean adminExists = false;
            for (Authority auth : user.getAuthorities()) {
                if (auth.getAuthority().equals("ROLE_ADMIN")) {
                    adminExists = true;
                    break;
                }
            }
            if (!adminExists && authorityName.equals("ADMIN")) {
                userService.addAuthority(username, "ROLE_ADMIN");
                return ResponseEntity.ok().body("Authority added");
            } else {
                return ResponseEntity.badRequest().body("Only ADMIN authority accepted OR already set");
            }
        }
        return ResponseEntity.badRequest().body("Username does not exist");
    }


    @DeleteMapping(value = "/authority")
    public ResponseEntity<Object> deleteUserAuthority(@RequestBody Map<String, Object> fields) throws ResourceNotFoundException {

        String authorityName = (String) fields.get("authority");
        String username = (String) fields.get("username");
        if (authorityName.equals("USER") || authorityName.equals("ADMIN")) {
            userService.removeAuthority(username, "ROLE_" + authorityName);
            return ResponseEntity.ok().body("Authority deleted");
        } else {
            return ResponseEntity.badRequest().body("Authority does not exist");
        }
    }

}
