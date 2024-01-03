package rs.raf.demo.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String jwt;
    private boolean canReadUsers;
    private boolean canCreateUsers;
    private boolean canUpdateUsers;
    private boolean canDeleteUsers;
    private boolean canSearchVacuum;
    private boolean canStartVacuum;
    private boolean canStopVacuum;
    private boolean canDischargeVacuum;
    private boolean canAddVacuum;
    private boolean canRemoveVacuum;


}
