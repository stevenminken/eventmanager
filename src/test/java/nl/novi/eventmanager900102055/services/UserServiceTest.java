package nl.novi.eventmanager900102055.services;

import nl.novi.eventmanager900102055.dtos.UserDto;
import nl.novi.eventmanager900102055.exceptions.NameDuplicateException;
import nl.novi.eventmanager900102055.exceptions.ResourceNotFoundException;
import nl.novi.eventmanager900102055.models.Authority;
import nl.novi.eventmanager900102055.models.User;
import nl.novi.eventmanager900102055.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private UserDto userDto;
    private User user;

    @BeforeEach
    public void setUp() {
        userDto = new UserDto();
        userDto.setUsername("gerrit_test");
        userDto.setPassword("password");
        userDto.setEmail("gerrit.test@example.com");
        userDto.setApikey("randomApiKey");
        userDto.setEnabled(true);

        user = new User();
        user.setUsername("gerrit_test");
        user.setPassword("password");
        user.setEmail("gerrit.test@example.com");
        user.setApikey("randomApiKey");
        user.setEnabled(true);
    }

    @Test
    @DisplayName("Create User with Valid Data Should Return Username")
    public void testCreateUser_WithValidData_ShouldReturnUsername() throws NameDuplicateException, ResourceNotFoundException {

        when(userRepository.findAll()).thenReturn(Collections.emptyList());
        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
        Set<Authority> authorities = user.getAuthorities();

        String username = userService.createUser(userDto);
        assertEquals("gerrit_test", username);

        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new Authority("gerrit_test", "ROLE_USER")));

    }


    @Test
    @DisplayName("Create User with Duplicate Username Should Throw NameDuplicateException")
    public void testCreateUser_WithDuplicateUsername_ShouldThrowNameDuplicateException() {
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));

        assertThrows(NameDuplicateException.class, () -> userService.createUser(userDto));
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Find All Users Should Return List of UserDto")
    public void testFindAllUsers_ShouldReturnListOfUserDto() {
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));

        List<UserDto> result = userService.findAllUsers();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("gerrit_test", result.get(0).getUsername());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Find User by Username with Valid Username Should Return UserDto")
    public void testFindUserByUsername_WithValidUsername_ShouldReturnUserDto() {
        when(userRepository.findByUsername("gerrit_test")).thenReturn(user);

        UserDto result = userService.findUserByUsername("gerrit_test");

        assertNotNull(result);
        assertEquals("gerrit_test", result.getUsername());
        assertEquals("gerrit.test@example.com", result.getEmail());
        verify(userRepository, times(1)).findByUsername(anyString());
    }

    @Test
    @DisplayName("Find User by Username with Invalid Username Should Throw UsernameNotFoundException")
    public void testFindUserByUsername_WithInvalidUsername_ShouldThrowUsernameNotFoundException() {
        when(userRepository.findByUsername(anyString())).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> userService.findUserByUsername("unknown_user"));
        verify(userRepository, times(1)).findByUsername(anyString());
    }
    @Test
    @DisplayName("Check if User Exists with Valid Username Should Return True")
    public void testUserExists_WithValidUsername_ShouldReturnTrue() {
        when(userRepository.existsById("gerrit_test")).thenReturn(true);

        boolean result = userService.userExists("gerrit_test");

        assertTrue(result);
        verify(userRepository, times(1)).existsById("gerrit_test");
    }

    @Test
    @DisplayName("Check if User Exists with Invalid Username Should Return False")
    public void testUserExists_WithInvalidUsername_ShouldReturnFalse() {
        when(userRepository.existsById("unknown_user")).thenReturn(false);

        boolean result = userService.userExists("unknown_user");

        assertFalse(result);
        verify(userRepository, times(1)).existsById("unknown_user");
    }
    @Test
    @DisplayName("Update User Password with Valid Username Should Update Password")
    public void testUpdateUserPassword_WithValidUsername_ShouldUpdatePassword() throws ResourceNotFoundException {
        when(userRepository.findByUsername("gerrit_test")).thenReturn(user);

        UserDto userDtoToUpdate = new UserDto();
        userDtoToUpdate.setUsername("gerrit_test");
        userDtoToUpdate.setPassword("new_password");

        userService.updateUserPassword(userDtoToUpdate);

        assertEquals("new_password", user.getPassword());
        verify(userRepository, times(1)).findByUsername("gerrit_test");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("Update User Password with Invalid Username Should Throw ResourceNotFoundException")
    public void testUpdateUserPassword_WithInvalidUsername_ShouldThrowResourceNotFoundException() {
        when(userRepository.findByUsername(anyString())).thenReturn(null);

        UserDto userDtoToUpdate = new UserDto();
        userDtoToUpdate.setUsername("unknown_user");
        userDtoToUpdate.setPassword("new_password");

        assertThrows(ResourceNotFoundException.class, () -> userService.updateUserPassword(userDtoToUpdate));
        verify(userRepository, times(1)).findByUsername(anyString());
    }
    @Test
    @DisplayName("Delete User with Existing Username Should Delete User")
    public void testDeleteUser_WithExistingUsername_ShouldDeleteUser() {
        when(userRepository.existsById("gerrit_test")).thenReturn(true);

        userService.deleteUser("gerrit_test");

        verify(userRepository, times(1)).existsById("gerrit_test");
        verify(userRepository, times(1)).deleteById("gerrit_test");
    }

    @Test
    @DisplayName("Delete User with Non-Existing Username Should Do Nothing")
    public void testDeleteUser_WithNonExistingUsername_ShouldDoNothing() {
        when(userRepository.existsById(anyString())).thenReturn(false);

        userService.deleteUser("unknown_user");

        verify(userRepository, times(1)).existsById("unknown_user");
        verify(userRepository, times(0)).deleteById(anyString());
    }
    @Test
    @DisplayName("Get Authorities for Existing User Should Return Authorities")
    public void testGetAuthorities_WithExistingUser_ShouldReturnAuthorities() throws ResourceNotFoundException {
        // Create a user with some authorities
        User user = new User();
        user.setUsername("gerrit_test");
        Authority authority1 = new Authority("gerrit_test", "ROLE_USER");
        Authority authority2 = new Authority("gerrit_test", "ROLE_ADMIN");
        user.addAuthority(authority1);
        user.addAuthority(authority2);

        when(userRepository.findById("gerrit_test")).thenReturn(Optional.of(user));

        Set<Authority> authorities = userService.getAuthorities("gerrit_test");

        assertEquals(2, authorities.size());
        assertTrue(authorities.contains(authority1));
        assertTrue(authorities.contains(authority2));
        verify(userRepository, times(1)).findById("gerrit_test");
    }

    @Test
    @DisplayName("Get Authorities for Non-Existing User Should Throw ResourceNotFoundException")
    public void testGetAuthorities_WithNonExistingUser_ShouldThrowResourceNotFoundException() {
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getAuthorities("unknown_user"));
        verify(userRepository, times(1)).findById(anyString());
    }

    @Test
    @DisplayName("Add Authority to Existing User Should Add Authority")
    public void testAddAuthority_WithExistingUser_ShouldAddAuthority() throws ResourceNotFoundException {
        // Create a user without authorities
        User user = new User();
        user.setUsername("gerrit_test");

        when(userRepository.findById("gerrit_test")).thenReturn(Optional.of(user));

        userService.addAuthority("gerrit_test", "ROLE_USER");

        Set<Authority> authorities = user.getAuthorities();
        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new Authority("gerrit_test", "ROLE_USER")));
        verify(userRepository, times(1)).findById("gerrit_test");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("Add Authority to Non-Existing User Should Throw ResourceNotFoundException")
    public void testAddAuthority_WithNonExistingUser_ShouldThrowResourceNotFoundException() {
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.addAuthority("unknown_user", "ROLE_USER"));
        verify(userRepository, times(1)).findById(anyString());
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    @DisplayName("Remove Authority from Existing User Should Remove Authority")
    public void testRemoveAuthority_WithExistingUser_ShouldRemoveAuthority() throws ResourceNotFoundException {
        // Create a user with some authorities
        User user = new User();
        user.setUsername("gerrit_test");
        Authority authority1 = new Authority("gerrit_test", "ROLE_USER");
        Authority authority2 = new Authority("gerrit_test", "ROLE_ADMIN");
        user.addAuthority(authority1);
        user.addAuthority(authority2);

        when(userRepository.findById("gerrit_test")).thenReturn(Optional.of(user));

        userService.removeAuthority("gerrit_test", "ROLE_ADMIN");

        Set<Authority> authorities = user.getAuthorities();
        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(authority1));
        assertFalse(authorities.contains(authority2));
        verify(userRepository, times(1)).findById("gerrit_test");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("Remove Authority from Non-Existing User Should Throw ResourceNotFoundException")
    public void testRemoveAuthority_WithNonExistingUser_ShouldThrowResourceNotFoundException() {
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.removeAuthority("unknown_user", "ROLE_USER"));
        verify(userRepository, times(1)).findById(anyString());
        verify(userRepository, times(0)).save(any(User.class));
    }
    @Test
    @DisplayName("Transfer User List to UserDto List")
    public void testTransferUserListToUserDtoList() {
        // Create some users for testing
        User user1 = new User();
        user1.setUsername("gerrit_test");
        user1.setEmail("gerrit.test@example.com");
        user1.addAuthority(new Authority("gerrit_test", "ROLE_USER"));
        user1.addAuthority(new Authority("gerrit_test", "ROLE_ADMIN"));

        User user2 = new User();
        user2.setUsername("emma_test");
        user2.setEmail("emma.test@example.com");
        user2.addAuthority(new Authority("emma_test", "ROLE_USER"));

        List<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);

        // Perform the transfer
        List<UserDto> userDtoList = userService.transferUserListToUserDtoList(userList);

        // Verify the results
        assertEquals(2, userDtoList.size());

        UserDto userDto1 = userDtoList.get(0);
        assertEquals("gerrit_test", userDto1.getUsername());
        assertEquals("gerrit.test@example.com", userDto1.getEmail());
        assertEquals(2, userDto1.getAuthorities().size());
        assertTrue(userDto1.getAuthorities().contains(new Authority("gerrit_test", "ROLE_USER")));
        assertTrue(userDto1.getAuthorities().contains(new Authority("gerrit_test", "ROLE_ADMIN")));

        UserDto userDto2 = userDtoList.get(1);
        assertEquals("emma_test", userDto2.getUsername());
        assertEquals("emma.test@example.com", userDto2.getEmail());
        assertEquals(1, userDto2.getAuthorities().size());
        assertTrue(userDto2.getAuthorities().contains(new Authority("emma_test", "ROLE_USER")));
    }

    @Test
    @DisplayName("Transfer UserDto to User")
    public void testTransferUserDtoToUser() {
        // Create a UserDto for testing
        UserDto userDto = new UserDto();
        userDto.setUsername("gerrit_test");
        userDto.setPassword("password");
        userDto.setEnabled(true);
        userDto.setApikey("apikey123");
        userDto.setEmail("gerrit.test@example.com");

        // Perform the transfer
        User user = userService.transferUserDtoToUser(userDto);

        // Verify the results
        assertEquals("gerrit_test", user.getUsername());
        assertEquals("password", user.getPassword());
        assertTrue(user.isEnabled());
        assertEquals("apikey123", user.getApikey());
        assertEquals("gerrit.test@example.com", user.getEmail());
//        assertNull(user.getAuthorities()); // Authorities are not transferred in this direction
    }

    @Test
    @DisplayName("Transfer User to UserDto")
    public void testTransferUserToUserDto() {
        // Create a User for testing
        User user = new User();
        user.setUsername("gerrit_test");
        user.setEmail("gerrit.test@example.com");
        user.addAuthority(new Authority("gerrit_test", "ROLE_USER"));
        user.addAuthority(new Authority("gerrit_test", "ROLE_ADMIN"));

        // Perform the transfer
        UserDto userDto = userService.transferUserToUserDto(user);

        // Verify the results
        assertEquals("gerrit_test", userDto.getUsername());
        assertEquals("gerrit.test@example.com", userDto.getEmail());
        assertEquals(2, userDto.getAuthorities().size());
        assertTrue(userDto.getAuthorities().contains(new Authority("gerrit_test", "ROLE_USER")));
        assertTrue(userDto.getAuthorities().contains(new Authority("gerrit_test", "ROLE_ADMIN")));
        assertNull(userDto.getPassword()); // Password is not transferred in this direction
        assertNull(userDto.getEnabled()); // Enabled is not transferred in this direction
        assertNull(userDto.getApikey()); // Apikey is not transferred in this direction
    }


}