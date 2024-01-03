package rs.raf.demo.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import rs.raf.demo.model.VacuumCleaner;
import rs.raf.demo.services.VacuumCleanerService;

@RestController
@RequestMapping("/cleaners")
@CrossOrigin
public class VacuumCleanerController {

    private final VacuumCleanerService vacuumCleanerService;

    @Autowired
    public VacuumCleanerController(VacuumCleanerService vacuumCleanerService) {
        this.vacuumCleanerService = vacuumCleanerService;
    }


//    @PreAuthorize("hasAuthority('can_delete_users')")
    @GetMapping("/{name}")
    public ResponseEntity<VacuumCleaner> create(@PathVariable("name") String name) {
        return ResponseEntity.ok(vacuumCleanerService.create(name, SecurityContextHolder.getContext().getAuthentication().getName()));
    }
}
