package nl.novi.eventmanager900102055.services;

import nl.novi.eventmanager900102055.dtos.UserDto;
import nl.novi.eventmanager900102055.exceptions.NameDuplicateException;
import nl.novi.eventmanager900102055.exceptions.ResourceNotFoundException;
import nl.novi.eventmanager900102055.models.User;
import nl.novi.eventmanager900102055.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDto> findAllUsers() {
        List<User> userList = userRepository.findAll();
        return transferUserListToUserDtoList(userList);
    }

    public UserDto createUser(UserDto userDto) throws NameDuplicateException {
        Iterable<User> allUsers = userRepository.findAll();
        for (User u: allUsers) {
            if (u.getName().equals(userDto.getName())) {
                throw new NameDuplicateException("This User already exists");
            }
        }
        User user = transferUserDtoToUser(userDto);
        return transferUserToUserDto(userRepository.save(user));
    }

    public UserDto findUserById(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return null;
        }
        return transferUserToUserDto(user);
    }
    public List<UserDto> getUserByName(String name) throws ResourceNotFoundException {
        Iterable<User> iterableUsers = userRepository.findByName(name);
        if(iterableUsers == null) {
            throw new ResourceNotFoundException("Can't find this user");
        }
        ArrayList<User> userList = new ArrayList<>();

        for (User user : iterableUsers) {
            userList.add(user);
        }

        return transferUserListToUserDtoList(userList);
    }

    public UserDto updateUser(Long id, UserDto userDto) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return null;
        }
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setName(user.getName());
        user.setAddress(userDto.getAddress());
        user.setPhoneNumber(userDto.getPhoneNumber());
        return transferUserToUserDto(userRepository.save(user));
    }

    public boolean deleteUser(Long id) {
        if(userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<UserDto> transferUserListToUserDtoList(List<User> userList) {
        List<UserDto> userDtoList = new ArrayList<>();

        for (User user : userList) {
            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setUsername(user.getUsername());
            userDto.setPassword(user.getPassword());
            userDto.setName(user.getName());
            userDto.setAddress(user.getAddress());
            userDto.setPhoneNumber(user.getPhoneNumber());
            userDtoList.add(userDto);
        }
        return userDtoList;
    }

    public User transferUserDtoToUser(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setName(user.getName());
        user.setAddress(userDto.getAddress());
        user.setPhoneNumber(userDto.getPhoneNumber());

        return user;
    }

    public UserDto transferUserToUserDto(User user) {
        UserDto userDto = new UserDto();

        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setPassword(user.getPassword());
        userDto.setName(user.getName());
        userDto.setAddress(user.getAddress());
        userDto.setPhoneNumber(user.getPhoneNumber());

        return userDto;
    }
}
