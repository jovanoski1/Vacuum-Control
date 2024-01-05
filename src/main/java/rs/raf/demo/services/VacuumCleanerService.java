package rs.raf.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import rs.raf.demo.model.ErrorMessage;
import rs.raf.demo.model.VacuumCleaner;
import rs.raf.demo.model.VacuumStatus;
import rs.raf.demo.repositories.ErrorMessageRepository;
import rs.raf.demo.repositories.UserRepository;
import rs.raf.demo.repositories.VacuumCleanerRepository;
import rs.raf.demo.requests.FilterVacuumRequest;
import rs.raf.demo.requests.ScheduleOperationRequest;
import rs.raf.demo.tasks.DischargeVacuumCleanerTask;
import rs.raf.demo.tasks.StartVacuumCleanerTask;
import rs.raf.demo.tasks.StopVacuumCleanerTask;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class VacuumCleanerService {

    private final VacuumCleanerRepository vacuumCleanerRepository;
    private final UserRepository userRepository;
    private final ErrorMessageRepository errorMessageRepository;

    @Autowired
    public VacuumCleanerService(VacuumCleanerRepository vacuumCleanerRepository, UserRepository userRepository, ErrorMessageRepository errorMessageRepository) {
        this.vacuumCleanerRepository = vacuumCleanerRepository;
        this.userRepository = userRepository;
        this.errorMessageRepository = errorMessageRepository;
    }

    public VacuumCleaner create(String name, String email){
        VacuumCleaner vacuumCleaner = new VacuumCleaner();
        vacuumCleaner.setName(name);
        vacuumCleaner.setOwner(userRepository.findUserByEmail(email));
        this.vacuumCleanerRepository.save(vacuumCleaner);
        return vacuumCleaner;
    }

    public List<VacuumCleaner> getAllByOwner(){
        return this.vacuumCleanerRepository.findVacuumCleanersByOwner(userRepository.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName()));
    }

    public VacuumCleaner removeCleaner(Long id){
        VacuumCleaner vacuumCleaner = this.vacuumCleanerRepository.getById(id);
        if (vacuumCleaner.getStatus().equals(VacuumStatus.STOPPED)){
            vacuumCleaner.setActive(false);
        }
        return this.vacuumCleanerRepository.save(vacuumCleaner);
    }

    public boolean startVC(Long id){
        VacuumCleaner vacuumCleaner = this.vacuumCleanerRepository.getById(id);

        if (!vacuumCleaner.getStatus().equals(VacuumStatus.STOPPED)){
            return false;
        }

        Thread newThread = new Thread(new StartVacuumCleanerTask(vacuumCleaner, vacuumCleanerRepository));
        newThread.start();

        return true;
    }

    public boolean stopVC(Long id){
        VacuumCleaner vacuumCleaner = this.vacuumCleanerRepository.getById(id);

        if (!vacuumCleaner.getStatus().equals(VacuumStatus.RUNNING)){
            return false;
        }

        Thread newThread = new Thread(new StopVacuumCleanerTask(vacuumCleaner, vacuumCleanerRepository));
        newThread.start();

        return true;
    }

    public boolean dischargeVC(Long id){
        VacuumCleaner vacuumCleaner = this.vacuumCleanerRepository.getById(id);

        if (!vacuumCleaner.getStatus().equals(VacuumStatus.STOPPED)){
            return false;
        }

        Thread newThread = new Thread(new DischargeVacuumCleanerTask(vacuumCleaner, vacuumCleanerRepository));
        newThread.start();

        return true;
    }

//    @Transactional
    public void scheduleStartVC(Long id){
        VacuumCleaner vacuumCleaner = this.vacuumCleanerRepository.findById(id).orElse(null);
        if (vacuumCleaner == null)return;
        System.out.println("------START-------");
        System.out.println(vacuumCleaner.getName());

        if (!vacuumCleaner.getStatus().equals(VacuumStatus.STOPPED)){
            ErrorMessage errorMessage = new ErrorMessage();
            errorMessage.setVacuumCleanerId(id);
            errorMessage.setTime(LocalDateTime.now());
            errorMessage.setOperation("START");
            errorMessage.setMessage("Chosen vacuum cleaner is not stopped!");
            errorMessageRepository.save(errorMessage);
            return;
        }

        Random r = new Random();
        try {
            Thread.sleep(15000 + r.nextInt(6)*1000);

            vacuumCleaner.setStatus(VacuumStatus.RUNNING);
            vacuumCleanerRepository.save(vacuumCleaner);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ObjectOptimisticLockingFailureException ex){
            ErrorMessage errorMessage = new ErrorMessage();
            errorMessage.setVacuumCleanerId(id);
            errorMessage.setTime(LocalDateTime.now());
            errorMessage.setOperation("START");
            errorMessage.setMessage("ObjectOptimisticLockingFailureException");
            errorMessageRepository.save(errorMessage);
        }

    }

//    @Transactional
    public void scheduleStopVC(Long id){
        VacuumCleaner vacuumCleaner = this.vacuumCleanerRepository.findById(id).orElse(null);
        if (vacuumCleaner == null) return;
        System.out.println("------STOP-------");
        System.out.println(vacuumCleaner.getName());

        if (!vacuumCleaner.getStatus().equals(VacuumStatus.RUNNING)){
            ErrorMessage errorMessage = new ErrorMessage();
            errorMessage.setVacuumCleanerId(id);
            errorMessage.setTime(LocalDateTime.now());
            errorMessage.setOperation("STOP");
            errorMessage.setMessage("Chosen vacuum cleaner is not running!");
            errorMessageRepository.save(errorMessage);
            return;
        }

        Random r = new Random();
        try {
            Thread.sleep(15000 + r.nextInt(6)*1000);

            vacuumCleaner.setStatus(VacuumStatus.STOPPED);
            vacuumCleaner.setNumOfCycles(vacuumCleaner.getNumOfCycles() + 1);
            vacuumCleaner = vacuumCleanerRepository.save(vacuumCleaner);

            if (vacuumCleaner.getNumOfCycles() % 3 == 0){
                Thread discharge = new Thread(new DischargeVacuumCleanerTask(vacuumCleaner,vacuumCleanerRepository));
                discharge.start();
            }

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ObjectOptimisticLockingFailureException ex){
            ErrorMessage errorMessage = new ErrorMessage();
            errorMessage.setVacuumCleanerId(id);
            errorMessage.setTime(LocalDateTime.now());
            errorMessage.setOperation("STOP");
            errorMessage.setMessage("ObjectOptimisticLockingFailureException");
            errorMessageRepository.save(errorMessage);
        }

    }


    public void scheduleDischargeVC(ScheduleOperationRequest operationRequest){
        VacuumCleaner vacuumCleaner = this.vacuumCleanerRepository.findById(operationRequest.getId()).orElse(null);
        if (vacuumCleaner == null)return;
        System.out.println("------DISCHARGE-------");
        System.out.println(vacuumCleaner.getName());

        Random r = new Random();
        try {
            int dev = r.nextInt(6) * 1000;
            Thread.sleep(15000 + dev/2);

            vacuumCleaner.setStatus(VacuumStatus.DISCHARGING);
            vacuumCleaner = vacuumCleanerRepository.save(vacuumCleaner);
//            this.vacuumCleanerRepository.setStatusForVc(VacuumStatus.DISCHARGING, id);

            System.out.println("PAUZA");
            Thread.sleep(15000 + dev/2);

//            this.vacuumCleanerRepository.setStatusForVc(VacuumStatus.STOPPED, id);

            vacuumCleaner.setStatus(VacuumStatus.STOPPED);
            vacuumCleaner.setNumOfCycles(0);
            vacuumCleanerRepository.save(vacuumCleaner);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ObjectOptimisticLockingFailureException ex){
            ErrorMessage errorMessage = new ErrorMessage();
            errorMessage.setVacuumCleanerId(operationRequest.getId());
            errorMessage.setTime(LocalDateTime.now());
            errorMessage.setOperation("DISCHARGE");
            errorMessage.setMessage("ObjectOptimisticLockingFailureException");
            errorMessageRepository.save(errorMessage);
        }

    }

    public List<VacuumCleaner> filter(FilterVacuumRequest request){
        List<VacuumCleaner> all = this.getAllByOwner();


        if (request.getName() != null) {
            all = all.stream()
                    .filter(vc -> vc.getName().toLowerCase().contains(request.getName().toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (request.getStatus() != null) {
            all = all.stream()
                    .filter(vc -> vc.getStatus().equals(request.getStatus()))
                    .collect(Collectors.toList());
        }

        if (request.getDateFrom() != null && request.getDateTo() != null) {
            LocalDateTime dateFrom = request.getDateFrom().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime dateTo = request.getDateTo().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

            all = all.stream()
                    .filter(vc -> {
                        LocalDateTime creationTime = vc.getCreationTime();
                        return creationTime.isAfter(dateFrom) && creationTime.isBefore(dateTo);
                    })
                    .collect(Collectors.toList());
        }

        return all;
    }
}
