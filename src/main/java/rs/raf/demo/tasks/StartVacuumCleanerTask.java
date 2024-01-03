package rs.raf.demo.tasks;

import org.springframework.orm.ObjectOptimisticLockingFailureException;
import rs.raf.demo.model.VacuumCleaner;
import rs.raf.demo.model.VacuumStatus;
import rs.raf.demo.repositories.VacuumCleanerRepository;

import java.util.Random;

public class StartVacuumCleanerTask implements Runnable{
    private final VacuumCleaner vacuumCleaner;
    private final VacuumCleanerRepository vacuumCleanerRepository;

    public StartVacuumCleanerTask(VacuumCleaner vacuumCleaner, VacuumCleanerRepository vacuumCleanerRepository) {
        this.vacuumCleaner = vacuumCleaner;
        this.vacuumCleanerRepository = vacuumCleanerRepository;
    }


    @Override
    public void run() {
        try {
            Random r = new Random();
            Thread.sleep(15000 + r.nextInt(6)*1000);

            vacuumCleaner.setStatus(VacuumStatus.RUNNING);
            vacuumCleanerRepository.save(vacuumCleaner);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }catch (ObjectOptimisticLockingFailureException ex){

            System.out.println("\u001B[31m" + ex.getMessage() + "\u001B[0m");
        }
    }
}
