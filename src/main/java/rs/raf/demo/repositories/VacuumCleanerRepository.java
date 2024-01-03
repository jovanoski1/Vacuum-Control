package rs.raf.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.raf.demo.model.User;
import rs.raf.demo.model.VacuumCleaner;

import java.util.List;

@Repository
public interface VacuumCleanerRepository extends JpaRepository<VacuumCleaner, Long> {

    List<VacuumCleaner> findVacuumCleanersByOwner(User owner);
}
