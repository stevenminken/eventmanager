package nl.novi.eventmanager900102055.services;

import nl.novi.eventmanager900102055.dtos.UserDto;
import nl.novi.eventmanager900102055.models.Authority;
import nl.novi.eventmanager900102055.models.User;
import nl.novi.eventmanager900102055.repositories.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {

        Optional<User> user = userRepository.findById(username);
        if (user.isPresent()) {
            UserDto userDto = transferUserToUserDto(user.get());
            String password = userDto.getPassword();

            Set<Authority> authorities = userDto.getAuthorities();
            List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
            for (Authority authority : authorities) {
                grantedAuthorities.add(new SimpleGrantedAuthority(authority.getAuthority()));
            }
            return new org.springframework.security.core.userdetails.User(username, password, grantedAuthorities);
        } else {
            throw new UsernameNotFoundException(username);
        }
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