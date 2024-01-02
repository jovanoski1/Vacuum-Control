package rs.raf.demo.responses;

import lombok.Data;

@Data
public class LoginResponse {
    private String jwt;
    private boolean canReadUsers;
    private boolean canCreateUsers;
    private boolean canUpdateUsers;
    private boolean canDeleteUsers;


    public LoginResponse(String jwt, boolean canCreateUsers,boolean canReadUsers, boolean canUpdateUsers, boolean canDeleteUsers) {
        this.jwt = jwt;
        this.canReadUsers = canReadUsers;
        this.canCreateUsers = canCreateUsers;
        this.canUpdateUsers = canUpdateUsers;
        this.canDeleteUsers = canDeleteUsers;
    }
}
