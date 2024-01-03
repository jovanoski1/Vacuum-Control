package rs.raf.demo.tasks;

import org.springframework.orm.ObjectOptimisticLockingFailureException;
import rs.raf.demo.model.VacuumCleaner;
import rs.raf.demo.model.VacuumStatus;
import rs.raf.demo.repositories.VacuumCleanerRepository;

import java.util.Random;

public class DischargeVacuumCleanerTask implements Runnable{
    private VacuumCleaner vacuumCleaner;
    private final VacuumCleanerRepository vacuumCleanerRepository;

    public DischargeVacuumCleanerTask(VacuumCleaner vacuumCleaner, VacuumCleanerRepository vacuumCleanerRepository) {
        this.vacuumCleaner = vacuumCleaner;
        this.vacuumCleanerRepository = vacuumCleanerRepository;
    }


    @Override
    public void run() {
        try {
            Random r = new Random();
            int dev = r.nextInt(6) * 1000;
            Thread.sleep(15000 + dev/2);

            vacuumCleaner.setStatus(VacuumStatus.DISCHARGING);
            vacuumCleaner = vacuumCleanerRepository.save(vacuumCleaner);

            Thread.sleep(15000 + dev/2);

            vacuumCleaner.setStatus(VacuumStatus.STOPPED);
            vacuumCleanerRepository.save(vacuumCleaner);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }catch (ObjectOptimisticLockingFailureException ex){

            System.out.println("\u001B[31m" + ex.getMessage() + "\u001B[0m");
        }
    }
}
