package org.tripleng.likesidehotel.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.tripleng.likesidehotel.config.jwt.JwtUtils;
import org.tripleng.likesidehotel.config.user.HotelUserDetails;
import org.tripleng.likesidehotel.exception.UserAlreadyExistException;
import org.tripleng.likesidehotel.model.Role;
import org.tripleng.likesidehotel.model.User;
import org.tripleng.likesidehotel.request.LoginRequest;
import org.tripleng.likesidehotel.response.JwtResponse;
import org.tripleng.likesidehotel.service.UserService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/auth/v1")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/register-user")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            String token =  userService.registerUser(user);
            return ResponseEntity.ok("Successful registration, please check your email to activate your account!");
        } catch (UserAlreadyExistException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error while registering!");
        }
    }

    @GetMapping("/confirm")
    public ResponseEntity<String> confirmationAccount(@RequestParam("token") String confirmationToken){
        try{
            return ResponseEntity.ok(userService.confirmToken(confirmationToken));
        }catch (IllegalStateException e){
            return  ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error confirmation token !");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@Valid @RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            // Additional logging
            logger.debug("User details: {}", authentication.getPrincipal());
            logger.debug("Encoded Password: {}", authentication.getCredentials());

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtTokenForUser(authentication);
            HotelUserDetails userDetails = (HotelUserDetails) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();
            return ResponseEntity.ok(new JwtResponse(
                    userDetails.getId(),
                    userDetails.getUsername(),
                    jwt,
                    roles
            ));
        } catch (BadCredentialsException e) {
            logger.error("Authentication failed. Bad credentials.", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bad credentials");
        }
    }

//    @PostMapping("/refresh-token")
//    public void refreshToken(
//            HttpServletRequest request,
//            HttpServletResponse response
//    ) throws IOException {
//        userService.refreshToken(request, response);
//    }
}
