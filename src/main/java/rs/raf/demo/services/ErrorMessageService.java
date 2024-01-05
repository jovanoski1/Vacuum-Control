package rs.raf.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.raf.demo.model.ErrorMessage;
import rs.raf.demo.model.VacuumCleaner;
import rs.raf.demo.repositories.ErrorMessageRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ErrorMessageService {

    private final ErrorMessageRepository errorMessageRepository;
    private final VacuumCleanerService vacuumCleanerService;

    @Autowired
    public ErrorMessageService(ErrorMessageRepository errorMessageRepository, VacuumCleanerService vacuumCleanerService) {
        this.errorMessageRepository = errorMessageRepository;
        this.vacuumCleanerService = vacuumCleanerService;
    }


    public List<ErrorMessage> getAllByOwner(){
        List<VacuumCleaner> myVcs= this.vacuumCleanerService.getAllByOwner();
        return this.errorMessageRepository.findErrorMessagesByVacuumCleanerIdIn(myVcs.stream()
                .map(VacuumCleaner::getId)
                .collect(Collectors.toList()));
    }
}
