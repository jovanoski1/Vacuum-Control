package rs.raf.demo.bootstrap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import rs.raf.demo.model.*;
import rs.raf.demo.repositories.*;

import java.util.HashSet;
import java.util.Set;

@Component
public class BootstrapData implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public BootstrapData(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

//        Role r1 = new Role();
//        r1.setRole("can_read_users");
//        roleRepository.save(r1);
//
//        Role r2 = new Role();
//        r2.setRole("can_create_users");
//        roleRepository.save(r2);
//
//        Role r3 = new Role();
//        r3.setRole("can_update_users");
//        roleRepository.save(r3);
//
//        Role r4 = new Role();
//        r4.setRole("can_delete_users");
//        roleRepository.save(r4);

//        System.out.println("Loading Data...");
//
//        User user1 = new User();
//        user1.setEmail("mihailjovanoski14@gmail.com");
//        user1.setPassword(this.passwordEncoder.encode("miha"));
//        user1.setFirstName("Mihail");
//        user1.setLastName("Jovanoski");
//
//        Set<Role> roles = new HashSet<>();
//        roles.add(r1);
//        roles.add(r2);
//        roles.add(r3);
//        roles.add(r4);
//        user1.setPermissions(roles);
//
//        this.userRepository.save(user1);
//
//        User user2 = new User();
//        user2.setEmail("user2");
//        user2.setPassword(this.passwordEncoder.encode("user2"));
//        this.userRepository.save(user2);
//
//        User user3 = new User();
//        user3.setEmail("user3");
//        user3.setPassword(this.passwordEncoder.encode("user3"));
//        this.userRepository.save(user3);

//        System.out.println("Data loaded!");
    }
}
