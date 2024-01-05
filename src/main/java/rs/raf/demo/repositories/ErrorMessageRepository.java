package rs.raf.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.raf.demo.model.ErrorMessage;

import java.util.Collection;
import java.util.List;

@Repository
public interface ErrorMessageRepository extends JpaRepository<ErrorMessage, Long> {

    List<ErrorMessage> findErrorMessagesByVacuumCleanerIdIn(Collection<Long> id);

}
