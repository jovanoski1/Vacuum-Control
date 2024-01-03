package rs.raf.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.raf.demo.model.VacuumCleaner;
import rs.raf.demo.model.VacuumStatus;
import rs.raf.demo.repositories.UserRepository;
import rs.raf.demo.repositories.VacuumCleanerRepository;
import rs.raf.demo.tasks.DischargeVacuumCleanerTask;
import rs.raf.demo.tasks.StartVacuumCleanerTask;
import rs.raf.demo.tasks.StopVacuumCleanerTask;

import java.util.List;

@Service
public class VacuumCleanerService {

    private final VacuumCleanerRepository vacuumCleanerRepository;
    private final UserRepository userRepository;

    @Autowired
    public VacuumCleanerService(VacuumCleanerRepository vacuumCleanerRepository, UserRepository userRepository) {
        this.vacuumCleanerRepository = vacuumCleanerRepository;
        this.userRepository = userRepository;
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
        vacuumCleaner.setActive(false);
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

}
