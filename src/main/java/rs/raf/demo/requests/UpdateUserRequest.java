package rs.raf.demo.requests;

import lombok.Data;
import rs.raf.demo.model.Role;

import java.util.List;

@Data
public class UpdateUserRequest {
    private String firstName;
    private String lastName;
    private String email;
    private List<Role> permissions;
}
