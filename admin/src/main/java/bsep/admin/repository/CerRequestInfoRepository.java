package bsep.admin.repository;

import bsep.admin.model.CerRequestInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CerRequestInfoRepository extends JpaRepository<CerRequestInfo, Integer> {
    CerRequestInfo findByUserId(int id);

    CerRequestInfo findByEmail(String email);
}
