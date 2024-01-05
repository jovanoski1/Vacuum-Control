package rs.raf.demo.requests;

import lombok.Data;
import rs.raf.demo.model.VacuumStatus;

import java.util.Date;

@Data
public class FilterVacuumRequest {
    private String name;
    private VacuumStatus status;
    private Date dateFrom;
    private Date dateTo;
}
