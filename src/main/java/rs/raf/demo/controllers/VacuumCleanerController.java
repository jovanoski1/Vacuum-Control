package rs.raf.demo.controllers;


import com.sun.xml.bind.v2.TODO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import rs.raf.demo.model.VacuumCleaner;
import rs.raf.demo.requests.FilterVacuumRequest;
import rs.raf.demo.requests.ScheduleOperationRequest;
import rs.raf.demo.services.VacuumCleanerService;
import rs.raf.demo.utils.Util;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/cleaners")
@CrossOrigin
public class VacuumCleanerController {

    private final VacuumCleanerService vacuumCleanerService;
    private final ThreadPoolTaskScheduler taskScheduler;

    @Autowired
    public VacuumCleanerController(VacuumCleanerService vacuumCleanerService, ThreadPoolTaskScheduler taskScheduler) {
        this.vacuumCleanerService = vacuumCleanerService;
        this.taskScheduler = taskScheduler;
        this.taskScheduler.setPoolSize(8);
        // TODO promeni da bude 1:M veza VC-ErrorMsg
    }


    @PreAuthorize("hasAuthority('can_add_vacuum')")
    @GetMapping("/{name}")
    public ResponseEntity<VacuumCleaner> create(@PathVariable("name") String name) {
        return ResponseEntity.ok(vacuumCleanerService.create(name, SecurityContextHolder.getContext().getAuthentication().getName()));
    }
    @PreAuthorize("hasAuthority('can_search_vacuum')")
    @GetMapping()
    public ResponseEntity<List<VacuumCleaner>> getAll(){
        return ResponseEntity.ok(vacuumCleanerService.getAllByOwner());
    }

    @PreAuthorize("hasAuthority('can_remove_vacuum')")
    @DeleteMapping("/remove/{id}")
    public ResponseEntity<VacuumCleaner> removeVC(@PathVariable("id") Long id) {
        return ResponseEntity.ok(vacuumCleanerService.removeCleaner(id));
    }

    @PreAuthorize("hasAuthority('can_start_vacuum')")
    @GetMapping("/start/{id}")
    public ResponseEntity<Integer> startVC(@PathVariable("id") Long id){
        return ResponseEntity.ok(vacuumCleanerService.startVC(id));
    }

    @PreAuthorize("hasAuthority('can_search_vacuum')")
    @PostMapping("/filter")
    public ResponseEntity<List<VacuumCleaner>> filter(@Valid @RequestBody FilterVacuumRequest request){
        return ResponseEntity.ok(vacuumCleanerService.filter(request));
    }

    @PreAuthorize("hasAuthority('can_stop_vacuum')")
    @GetMapping("/stop/{id}")
    public ResponseEntity<Integer> stopVC(@PathVariable("id") Long id){
        return ResponseEntity.ok(vacuumCleanerService.stopVC(id));
    }

    @PreAuthorize("hasAuthority('can_discharge_vacuum')")
    @GetMapping("/discharge/{id}")
    public ResponseEntity<Integer> dischargeVC(@PathVariable("id") Long id){
        return ResponseEntity.ok(vacuumCleanerService.dischargeVC(id));
    }

    @PreAuthorize("hasAuthority('can_start_vacuum')")
    @PostMapping("/scheduleStart")
    public ResponseEntity<Boolean> scheduleStartVC(@Valid @RequestBody ScheduleOperationRequest operationRequest){

        System.out.println(Util.convertTimeToCron(operationRequest.getDateTime()));
//        operationRequest.setDateTime(LocalDateTime.now().plusSeconds(2));

        CronTrigger cron = new CronTrigger(Util.convertTimeToCron(operationRequest.getDateTime()));
        this.taskScheduler.schedule(() -> this.vacuumCleanerService.scheduleStartVC(operationRequest.getId()), cron);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('can_stop_vacuum')")
    @PostMapping("/scheduleStop")
    public ResponseEntity<Boolean> scheduleStopVC(@Valid @RequestBody ScheduleOperationRequest operationRequest){

        System.out.println(Util.convertTimeToCron(operationRequest.getDateTime()));
//        operationRequest.setDateTime(LocalDateTime.now().plusSeconds(2));

        CronTrigger cron = new CronTrigger(Util.convertTimeToCron(operationRequest.getDateTime()));
        this.taskScheduler.schedule(() -> this.vacuumCleanerService.scheduleStopVC(operationRequest.getId()), cron);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('can_discharge_vacuum')")
    @PostMapping("/scheduleDischarge")
    public ResponseEntity<Boolean> scheduleDischargeVC(@Valid @RequestBody ScheduleOperationRequest operationRequest){

        System.out.println(Util.convertTimeToCron(operationRequest.getDateTime()));
//        operationRequest.setDateTime(LocalDateTime.now().plusSeconds(2));

        CronTrigger cron = new CronTrigger(Util.convertTimeToCron(operationRequest.getDateTime()));
        this.taskScheduler.schedule(() -> this.vacuumCleanerService.scheduleDischargeVC(operationRequest), cron);
//        this.vacuumCleanerService.scheduleDischargeVC(operationRequest);
        return ResponseEntity.ok().build();
    }
}
