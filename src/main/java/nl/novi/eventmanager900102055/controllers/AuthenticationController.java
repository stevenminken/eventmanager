package nl.novi.eventmanager900102055.controllers;

import nl.novi.eventmanager900102055.dtos.AuthenticationRequest;
import nl.novi.eventmanager900102055.dtos.AuthenticationResponse;
import nl.novi.eventmanager900102055.utils.JwtUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@CrossOrigin
@RestController
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtl;

    public AuthenticationController(AuthenticationManager authenticationManager, JwtUtil jwtUtl) {
        this.authenticationManager = authenticationManager;
        this.jwtUtl = jwtUtl;
    }

    /*
        Deze methode geeft de principal (basis user gegevens) terug van de ingelogde gebruiker
    */
    @GetMapping(value = "/authenticated")
    public ResponseEntity<Object> authenticated(Authentication authentication, Principal principal) {
        return ResponseEntity.ok().body(principal);
    }

    /*
    Deze methode geeft het JWT token terug wanneer de gebruiker de juiste inloggegevens op geeft.
     */
    @PostMapping(value = "/authenticate")

    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {

        String username = authenticationRequest.getUsername();
        String password = authenticationRequest.getPassword();

        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));

            final UserDetails userDetails = (UserDetails) auth.getPrincipal();
            final String jwt = jwtUtl.generateToken(userDetails);

            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                    .body("Token generated: " + new AuthenticationResponse(jwt).getJwt());
        } catch (BadCredentialsException ex) {
            throw new Exception("Incorrect username or password", ex);
        }
    }
}
