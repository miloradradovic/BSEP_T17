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
        return revocationRequestRepository.findAll();
    }

    @Override
    public Page<CerRevocationRequest> findAll(Pageable pageable) {
        return revocationRequestRepository.findAll(pageable);
    }

    @Override
    public CerRevocationRequest findOne(int id) {
        return revocationRequestRepository.findById(id).orElse(null);
    }

    @Override
    public CerRevocationRequest saveOne(CerRevocationRequest entity) {
        return revocationRequestRepository.save(entity);
    }

    @Override
    public boolean delete(int id) {
        CerRevocationRequest cerRevocationRequest = revocationRequestRepository.findById(id).orElse(null);
        if (cerRevocationRequest != null) {
            revocationRequestRepository.delete(cerRevocationRequest);
            return true;
        }
        return false;
    }

    @Override
    public CerRevocationRequest update(CerRevocationRequest entity) {
        CerRevocationRequest cerRevocationRequest = revocationRequestRepository.findById(entity.getId()).orElse(null);
        if (cerRevocationRequest != null) {
            entity.setId(cerRevocationRequest.getId());
            return revocationRequestRepository.save(entity);
        }
        return null;
    }
}
