package rs.raf.demo.tasks;

import org.springframework.orm.ObjectOptimisticLockingFailureException;
import rs.raf.demo.model.VacuumCleaner;
import rs.raf.demo.model.VacuumStatus;
import rs.raf.demo.repositories.VacuumCleanerRepository;

import java.util.Random;

public class StopVacuumCleanerTask implements Runnable{
    private VacuumCleaner vacuumCleaner;
    private final VacuumCleanerRepository vacuumCleanerRepository;

    public StopVacuumCleanerTask(VacuumCleaner vacuumCleaner, VacuumCleanerRepository vacuumCleanerRepository) {
        this.vacuumCleaner = vacuumCleaner;
        this.vacuumCleanerRepository = vacuumCleanerRepository;
    }


    @Override
    public void run() {
        try {
            Random r = new Random();
            Thread.sleep(15000 + r.nextInt(6)*1000);

            vacuumCleaner.setStatus(VacuumStatus.STOPPED);
            vacuumCleaner.setNumOfCycles(vacuumCleaner.getNumOfCycles() + 1);
            vacuumCleaner = vacuumCleanerRepository.save(vacuumCleaner);

            if (vacuumCleaner.getNumOfCycles() % 3 == 0){
                Thread discharge = new Thread(new DischargeVacuumCleanerTask(vacuumCleaner,vacuumCleanerRepository));
                discharge.start();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }catch (ObjectOptimisticLockingFailureException ex){

            System.out.println("\u001B[31m" + ex.getMessage() + "\u001B[0m");
        }
    }
}
