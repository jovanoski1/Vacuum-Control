package rs.raf.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import rs.raf.demo.model.Role;
import rs.raf.demo.model.User;
import rs.raf.demo.requests.CreateUserRequest;
import rs.raf.demo.requests.UpdateUserRequest;
import rs.raf.demo.services.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasAuthority('can_create_users')")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> create(@Valid @RequestBody CreateUserRequest user) {
//        if (!SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new Role("can_create_users"))) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
//        }
//        System.out.println();
//        System.out.println(SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        return ResponseEntity.ok(userService.create(user)); // this.userService.create(user);
    }

    @PreAuthorize("hasAuthority('can_read_users')")
    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        return ResponseEntity.ok(userService.getAll());
    }

    @PreAuthorize("hasAuthority('can_update_users')")
    @PostMapping("/update")
    public ResponseEntity<User> update(@Valid @RequestBody UpdateUserRequest user) {
        return ResponseEntity.ok(userService.update(user));
    }

    @PreAuthorize("hasAuthority('can_delete_users')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable("id") Long id) {
        userService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public User me() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return this.userService.findByEmail(email);
    }
}
