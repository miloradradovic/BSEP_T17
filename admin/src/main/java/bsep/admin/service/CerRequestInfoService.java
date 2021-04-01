package bsep.admin.service;

import bsep.admin.model.CerRequestInfo;
import bsep.admin.repository.CerRequestInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CerRequestInfoService implements ServiceInterface<CerRequestInfo> {

    @Autowired
    CerRequestInfoRepository cerRequestInfoRepository;


    @Override
    public List<CerRequestInfo> findAll() {
        return cerRequestInfoRepository.findAll();
    }

    @Override
    public Page<CerRequestInfo> findAll(Pageable pageable) {
        return cerRequestInfoRepository.findAll(pageable);
    }

    @Override
    public CerRequestInfo findOne(int id) {
        return cerRequestInfoRepository.findById(id).orElse(null);
    }

    @Override
    public CerRequestInfo saveOne(CerRequestInfo entity) {
        CerRequestInfo cerRequestInfo = cerRequestInfoRepository.findByUserId(entity.getUserId());

        return cerRequestInfo == null ? cerRequestInfoRepository.save(entity) : null;

    }

    @Override
    public boolean delete(int id) {
        CerRequestInfo cerRequestInfo = findOne(id);
        if (cerRequestInfo != null) {
            cerRequestInfoRepository.delete(cerRequestInfo);
            return true;
        }
        return false;

    }

    @Override
    public CerRequestInfo update(CerRequestInfo entity) {
        return null;
    }
}
