package bsep.admin.service;

import bsep.admin.model.CerRevocationRequest;
import bsep.admin.repository.CerRevocationRequestRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CerRevocationRequestService implements ServiceInterface<CerRevocationRequest> {

    @Autowired
    CerRevocationRequestRepository revocationRequestRepository;

    private static Logger logger = LogManager.getLogger(CerRevocationRequest.class);


    @Override
    public List<CerRevocationRequest> findAll() {
        logger.info("Getting all certificate revocation requests.");
        return revocationRequestRepository.findAll();
    }

    @Override
    public Page<CerRevocationRequest> findAll(Pageable pageable) {
        logger.info("Getting all certificate revocation requests paged.");
        return revocationRequestRepository.findAll(pageable);
    }

    @Override
    public CerRevocationRequest findOne(int id) {
        logger.info("Getting certificate revocation request by id.");
        return revocationRequestRepository.findById(id).orElse(null);
    }

    @Override
    public CerRevocationRequest saveOne(CerRevocationRequest entity) {
        logger.info("Saving certificate revocation request.");
        return revocationRequestRepository.save(entity);
    }

    @Override
    public boolean delete(int id) {
        logger.info("Attempting to delete certificate revocation request.");
        CerRevocationRequest cerRevocationRequest = revocationRequestRepository.findById(id).orElse(null);
        if (cerRevocationRequest != null) {
            revocationRequestRepository.delete(cerRevocationRequest);
            logger.info("Successfully deleted certificate revocation request.");
            return true;
        }
        logger.error("Failed to delete certificate revocation request.");
        return false;
    }

    @Override
    public CerRevocationRequest update(CerRevocationRequest entity) {
        logger.info("Attempting to update certificate revocation request.");
        CerRevocationRequest cerRevocationRequest = revocationRequestRepository.findById(entity.getId()).orElse(null);
        if (cerRevocationRequest != null) {
            entity.setId(cerRevocationRequest.getId());
            logger.info("Successfully updated certificate revocation request.");
            return revocationRequestRepository.save(entity);
        }
        logger.error("Failed to update certificate revocation request.");
        return null;
    }
}
