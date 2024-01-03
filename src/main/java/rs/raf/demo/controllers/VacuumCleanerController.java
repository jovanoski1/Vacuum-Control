package rs.raf.demo.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import rs.raf.demo.model.VacuumCleaner;
import rs.raf.demo.services.VacuumCleanerService;

import java.util.List;

@RestController
@RequestMapping("/cleaners")
@CrossOrigin
public class VacuumCleanerController {

    private final VacuumCleanerService vacuumCleanerService;

    @Autowired
    public VacuumCleanerController(VacuumCleanerService vacuumCleanerService) {
        this.vacuumCleanerService = vacuumCleanerService;
    }


    @PreAuthorize("hasAuthority('can_add_vacuum')")
    @GetMapping("/{name}")
    public ResponseEntity<VacuumCleaner> create(@PathVariable("name") String name) {
        return ResponseEntity.ok(vacuumCleanerService.create(name, SecurityContextHolder.getContext().getAuthentication().getName()));
    }

    @GetMapping()
    public ResponseEntity<List<VacuumCleaner>> getAll(){
        return ResponseEntity.ok(vacuumCleanerService.getAllByOwner(SecurityContextHolder.getContext().getAuthentication().getName()));
    }

    @PreAuthorize("hasAuthority('can_remove_vacuum')")
    @PostMapping("/remove/{id}")
    public ResponseEntity<VacuumCleaner> removeVC(@PathVariable("id") Long id) {
        return ResponseEntity.ok(vacuumCleanerService.removeCleaner(id));
    }

    @PreAuthorize("hasAuthority('can_start_vacuum')")
    @GetMapping("/start/{id}")
    public ResponseEntity<Boolean> startVC(@PathVariable("id") Long id){
        return ResponseEntity.ok(vacuumCleanerService.startVC(id));
    }

    @PreAuthorize("hasAuthority('can_stop_vacuum')")
    @GetMapping("/stop/{id}")
    public ResponseEntity<Boolean> stopVC(@PathVariable("id") Long id){
        return ResponseEntity.ok(vacuumCleanerService.stopVC(id));
    }

    @PreAuthorize("hasAuthority('can_discharge_vacuum')")
    @GetMapping("/discharge/{id}")
    public ResponseEntity<Boolean> dischargeVC(@PathVariable("id") Long id){
        return ResponseEntity.ok(vacuumCleanerService.dischargeVC(id));
    }
}
