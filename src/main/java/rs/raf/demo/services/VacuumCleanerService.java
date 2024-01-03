package rs.raf.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.raf.demo.model.VacuumCleaner;
import rs.raf.demo.repositories.UserRepository;
import rs.raf.demo.repositories.VacuumCleanerRepository;

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
        vacuumCleanerRepository.save(vacuumCleaner);
        return vacuumCleaner;
    }

}
