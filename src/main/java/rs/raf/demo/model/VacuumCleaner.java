package rs.raf.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

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
    private int numOfCycles = 0;
    private LocalDateTime creationTime = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "addedBy", referencedColumnName = "userId")
    @JsonIgnore
    private User owner;

    @Version
    @JsonIgnore
    private Integer version;
}
