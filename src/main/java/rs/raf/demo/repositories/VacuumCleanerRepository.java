package rs.raf.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.raf.demo.model.VacuumCleaner;

@Repository
public interface VacuumCleanerRepository extends JpaRepository<VacuumCleaner, Long> {

}
