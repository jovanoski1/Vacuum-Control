package rs.raf.demo.tasks;

import org.springframework.orm.ObjectOptimisticLockingFailureException;
import rs.raf.demo.model.VacuumCleaner;
import rs.raf.demo.model.VacuumStatus;
import rs.raf.demo.repositories.VacuumCleanerRepository;

import java.util.Random;

public class DischargeVacuumCleanerTask implements Runnable{
    private VacuumCleaner vacuumCleaner;
    private final VacuumCleanerRepository vacuumCleanerRepository;
    private final int waitTime;

    public DischargeVacuumCleanerTask(VacuumCleaner vacuumCleaner, VacuumCleanerRepository vacuumCleanerRepository, int waitTime) {
        this.vacuumCleaner = vacuumCleaner;
        this.vacuumCleanerRepository = vacuumCleanerRepository;
        this.waitTime = waitTime;
    }


    @Override
    public void run() {
        try {
            Random r = new Random();

            Thread.sleep(waitTime/2);

            vacuumCleaner.setStatus(VacuumStatus.DISCHARGING);
            vacuumCleaner = vacuumCleanerRepository.save(vacuumCleaner);

            Thread.sleep(waitTime/2);

            vacuumCleaner.setStatus(VacuumStatus.STOPPED);
            vacuumCleaner.setNumOfCycles(0);
            vacuumCleanerRepository.save(vacuumCleaner);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }catch (ObjectOptimisticLockingFailureException ex){

            System.out.println("\u001B[31m" + ex.getMessage() + "\u001B[0m");
        }
    }
}
