package rs.raf.demo.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@Entity
public class VacuumCleaner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Name is mandatory!")
    private String name;
    private VacuumStatus status = VacuumStatus.STOPPED;
    private boolean active = true;

    @ManyToOne
    @JoinColumn(name = "addedBy", referencedColumnName = "userId")
    private User owner;
}
