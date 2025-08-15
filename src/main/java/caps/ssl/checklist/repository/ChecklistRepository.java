package caps.ssl.checklist.repository;

import caps.ssl.checklist.model.Checklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChecklistRepository extends JpaRepository<Checklist, Long> {

    Optional<Checklist> findByContractId(Long contractId);
}
