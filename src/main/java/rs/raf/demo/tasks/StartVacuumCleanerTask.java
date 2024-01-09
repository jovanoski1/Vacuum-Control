package rs.raf.demo.tasks;

import org.springframework.orm.ObjectOptimisticLockingFailureException;
import rs.raf.demo.model.VacuumCleaner;
import rs.raf.demo.model.VacuumStatus;
import rs.raf.demo.repositories.VacuumCleanerRepository;

import java.util.Random;

public class StartVacuumCleanerTask implements Runnable{
    private final VacuumCleaner vacuumCleaner;
    private final VacuumCleanerRepository vacuumCleanerRepository;
    private int waitTime;

    public StartVacuumCleanerTask(VacuumCleaner vacuumCleaner, VacuumCleanerRepository vacuumCleanerRepository, int waitTime) {
        this.vacuumCleaner = vacuumCleaner;
        this.vacuumCleanerRepository = vacuumCleanerRepository;
        this.waitTime=waitTime;
    }


    @Override
    public void run() {
        try {
            Thread.sleep(waitTime);

            vacuumCleaner.setStatus(VacuumStatus.RUNNING);
            vacuumCleanerRepository.save(vacuumCleaner);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }catch (ObjectOptimisticLockingFailureException ex){

            System.out.println("\u001B[31m" + ex.getMessage() + "\u001B[0m");
        }
    }
}
