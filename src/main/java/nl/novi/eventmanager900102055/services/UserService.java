package nl.novi.eventmanager900102055.services;

import nl.novi.eventmanager900102055.dtos.UserDto;
import nl.novi.eventmanager900102055.exceptions.NameDuplicateException;
import nl.novi.eventmanager900102055.exceptions.ResourceNotFoundException;
import nl.novi.eventmanager900102055.models.Authority;
import nl.novi.eventmanager900102055.models.User;
import nl.novi.eventmanager900102055.repositories.UserRepository;
import nl.novi.eventmanager900102055.utils.RandomStringGenerator;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String createUser(UserDto userDto) throws NameDuplicateException {

        Iterable<User> Users = userRepository.findAll();
        for (User user : Users) {
            if (user.getUsername().equals(userDto.getUsername())) {
                throw new NameDuplicateException("This user already exists");
            }
        }
        String randomString = RandomStringGenerator.generateAlphaNumeric(20);
        userDto.setApikey(randomString);
        User user = transferUserDtoToUser(userDto);

        user = userRepository.save(user);
        return user.getUsername();
    }

    public List<UserDto> findAllUsers() {
        List<User> userList = userRepository.findAll();
        return transferUserListToUserDtoList(userList);
    }

    public UserDto findUserByUsername(String username) {
        UserDto dto = new UserDto();
        Optional<User> user = userRepository.findById(username);
        if (user.isPresent()) {
            dto = transferUserToUserDto(user.get());
        } else {
            throw new UsernameNotFoundException(username);
        }
        return dto;
    }

    public boolean userExists(String username) {
        return userRepository.existsById(username);
    }

    public void updateUser(String username, UserDto newUser) throws ResourceNotFoundException {
        User user = userRepository.findById(username).orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        user.setUsername(newUser.getUsername());
        user.setPassword(newUser.getPassword());
        user.setEnabled(newUser.getEnabled());
        user.setApikey(newUser.getApikey());
        user.setEmail(newUser.getEmail());
        userRepository.save(user);
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

        return user;
    }

    public UserDto transferUserToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setPassword(user.getPassword());
        userDto.setEnabled(user.isEnabled());
        userDto.setApikey(user.getApikey());
        userDto.setEmail(user.getEmail());
        userDto.setAuthorities(user.getAuthorities());
        return userDto;
    }

}
