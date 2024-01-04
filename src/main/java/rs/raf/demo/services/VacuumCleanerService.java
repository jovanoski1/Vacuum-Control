package rs.raf.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import rs.raf.demo.model.ErrorMessage;
import rs.raf.demo.model.VacuumCleaner;
import rs.raf.demo.model.VacuumStatus;
import rs.raf.demo.repositories.ErrorMessageRepository;
import rs.raf.demo.repositories.UserRepository;
import rs.raf.demo.repositories.VacuumCleanerRepository;
import rs.raf.demo.requests.ScheduleOperationRequest;
import rs.raf.demo.tasks.DischargeVacuumCleanerTask;
import rs.raf.demo.tasks.StartVacuumCleanerTask;
import rs.raf.demo.tasks.StopVacuumCleanerTask;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

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

    public List<VacuumCleaner> getAllByOwner(String email){
        return this.vacuumCleanerRepository.findVacuumCleanersByOwner(userRepository.findUserByEmail(email));
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

    @Transactional
    public void scheduleStartVC(Long id){
        VacuumCleaner vacuumCleaner = this.vacuumCleanerRepository.getById(id);
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
            errorMessage.setMessage(ex.getMessage());
            errorMessageRepository.save(errorMessage);
        }

    }

    @Transactional
    public void scheduleStopVC(Long id){
        VacuumCleaner vacuumCleaner = this.vacuumCleanerRepository.getById(id);
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
            errorMessage.setMessage(ex.getMessage());
            errorMessageRepository.save(errorMessage);
        }

    }

}
