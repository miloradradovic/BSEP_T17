package bsep.admin.repository;

import bsep.admin.model.CerRevocationRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CerRevocationRequestRepository extends JpaRepository<CerRevocationRequest, Integer> {
}
