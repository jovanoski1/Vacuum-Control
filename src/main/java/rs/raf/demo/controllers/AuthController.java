package rs.raf.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import rs.raf.demo.model.User;
import rs.raf.demo.requests.LoginRequest;
import rs.raf.demo.responses.LoginResponse;
import rs.raf.demo.services.UserService;
import rs.raf.demo.utils.JwtUtil;

@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager, UserService userService, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        } catch (Exception   e){
            e.printStackTrace();
            return ResponseEntity.status(401).build();
        }

        User u = userService.findByEmail(loginRequest.getEmail());

        boolean canCreateUsers = u.getPermissions().stream().anyMatch(role -> role.getRole().equals("can_create_users"));
        boolean canReadUsers = u.getPermissions().stream().anyMatch(role -> role.getRole().equals("can_read_users"));
        boolean canUpdateUsers = u.getPermissions().stream().anyMatch(role -> role.getRole().equals("can_update_users"));
        boolean canDeleteUsers = u.getPermissions().stream().anyMatch(role -> role.getRole().equals("can_delete_users"));

        boolean canSearchVacuum = u.getPermissions().stream().anyMatch(role -> role.getRole().equals("can_search_vacuum"));
        boolean canStartVacuum = u.getPermissions().stream().anyMatch(role -> role.getRole().equals("can_start_vacuum"));
        boolean canStopVacuum = u.getPermissions().stream().anyMatch(role -> role.getRole().equals("can_stop_vacuum"));
        boolean canDischargeVacuum = u.getPermissions().stream().anyMatch(role -> role.getRole().equals("can_discharge_vacuum"));
        boolean canAddVacuum = u.getPermissions().stream().anyMatch(role -> role.getRole().equals("can_add_vacuum"));
        boolean canRemoveVacuum = u.getPermissions().stream().anyMatch(role -> role.getRole().equals("can_remove_vacuum"));

        return ResponseEntity.ok(
                new LoginResponse(
                        jwtUtil.generateToken(loginRequest.getEmail()),
                        canCreateUsers,
                        canReadUsers,
                        canUpdateUsers,
                        canDeleteUsers,
                        canSearchVacuum,
                        canStartVacuum,
                        canStopVacuum,
                        canDischargeVacuum,
                        canAddVacuum,
                        canRemoveVacuum
                )
        );
    }

}
