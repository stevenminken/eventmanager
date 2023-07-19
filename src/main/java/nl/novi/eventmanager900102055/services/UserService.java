package nl.novi.eventmanager900102055.services;

import nl.novi.eventmanager900102055.dtos.UserDto;
import nl.novi.eventmanager900102055.exceptions.NameDuplicateException;
import nl.novi.eventmanager900102055.exceptions.ResourceNotFoundException;
import nl.novi.eventmanager900102055.models.Authority;
import nl.novi.eventmanager900102055.models.User;
import nl.novi.eventmanager900102055.repositories.UserRepository;
import nl.novi.eventmanager900102055.utils.RandomStringGenerator;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String createUser(UserDto userDto) throws NameDuplicateException, ResourceNotFoundException {

        Iterable<User> users = userRepository.findAll();
        for (User user : users) {
            if (user.getUsername().equals(userDto.getUsername())) {
                throw new NameDuplicateException("This user already exists");
            }
        }

        String randomString = RandomStringGenerator.generateAlphaNumeric(20);
        userDto.setApikey(randomString);

        String encodedPassword = passwordEncoder.encode(userDto.getPassword());
        userDto.setPassword(encodedPassword);

        User user = transferUserDtoToUser(userDto);
        user = userRepository.save(user);

        addAuthority(user.getUsername(), "ROLE_USER");
        return user.getUsername();
    }

    public List<UserDto> findAllUsers() {
        List<User> userList = userRepository.findAll();
        return transferUserListToUserDtoList(userList);
    }

    public UserDto findUserByUsername(String username) {
        UserDto dto = new UserDto();
        User user = userRepository.findByUsername(username);
        if (user != null) {
            dto = transferUserToUserDto(user);
        } else {
            throw new UsernameNotFoundException(username);
        }
        return dto;
    }

    public boolean userExists(String username) {
        return userRepository.existsById(username);
    }

    public void updateUserPassword(UserDto userDto) throws ResourceNotFoundException {
        User user = userRepository.findByUsername(userDto.getUsername());
        if (user == null) {
            throw new ResourceNotFoundException("User does not exist");
        } else {
            user.setPassword(userDto.getPassword());
            userRepository.save(user);
        }
    }

    public void deleteUser(String username) {
        if (userExists(username)) {
            userRepository.deleteById(username);
        }
    }

    public Set<Authority> getAuthorities(String username) throws ResourceNotFoundException {
        User user = userRepository.findById(username).orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        UserDto userDto = transferUserToUserDto(user);
        return userDto.getAuthorities();
    }

    public void addAuthority(String username, String authority) throws ResourceNotFoundException {
        User user = userRepository.findById(username).orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        user.addAuthority(new Authority(username, authority));
        userRepository.save(user);
    }

    public void removeAuthority(String username, String authority) throws ResourceNotFoundException {
        User user = userRepository.findById(username).orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        Optional<Authority> optionalAuthorityToRemove = user.getAuthorities()
                .stream()
                .filter(a -> a.getAuthority().equalsIgnoreCase(authority))
                .findAny();

        if (optionalAuthorityToRemove.isPresent()) {
            Authority authorityToRemove = optionalAuthorityToRemove.get();
            user.removeAuthority(authorityToRemove);
            userRepository.save(user);
        } else {
            throw new ResourceNotFoundException("Authority not found: " + authority);
        }
    }

    public List<UserDto> transferUserListToUserDtoList(List<User> userList) {
        return userList.stream()
                .map(this::transferUserToUserDto)
                .collect(Collectors.toList());
    }

    public User transferUserDtoToUser(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setEnabled(userDto.getEnabled());
        user.setApikey(userDto.getApikey());
        user.setEmail(userDto.getEmail());
        user.setTicketList(userDto.getTicketList());

        return user;
    }

    public UserDto transferUserToUserDto(User user) {
        UserDto userDto = new UserDto();
        for (Authority auth : user.getAuthorities()) {
            if (auth.getAuthority().equals("ROLE_ADMIN")) {
                userDto.setUsername(user.getUsername());
                userDto.setEmail(user.getEmail());
                userDto.setAuthorities(user.getAuthorities());
                userDto.setTicketList(user.getTicketList());
                break;
            } else {
                userDto.setUsername(user.getUsername());
                userDto.setEmail(user.getEmail());
                userDto.setTicketList(user.getTicketList());
            }
        }
        return userDto;
//        if (auth.getPrincipal() instanceof UserDetails) {
//            UserDetails ud = (UserDetails) auth.getPrincipal();
//            return ResponseEntity.status(HttpStatus.CONFLICT).body("You can't do that now crazy " + ud.getUsername() + " You are a " + ud.getAuthorities());
    }
}
