package rs.raf.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rs.raf.demo.model.User;
import rs.raf.demo.model.VacuumCleaner;
import rs.raf.demo.model.VacuumStatus;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface VacuumCleanerRepository extends JpaRepository<VacuumCleaner, Long> {

    List<VacuumCleaner> findVacuumCleanersByOwner(User owner);

    @Modifying
    @Query("update VacuumCleaner vc set vc.status = :status where vc.id = :id  and vc.status = '1'")
    @Transactional
    public void setStatusForVc1(@Param("status") VacuumStatus status, @Param("id") Long id);

    @Modifying
    @Query("update VacuumCleaner vc set vc.status = :status, vc.numOfCycles = vc.numOfCycles + 1 where vc.id = :id and vc.status = '2'")
    @Transactional
    public void setStatusForVc2(@Param("status") VacuumStatus status, @Param("id") Long id);
}
