package bsep.admin.mappers;

import bsep.admin.dto.CerRequestInfoDTO;
import bsep.admin.model.CerRequestInfo;

import java.util.ArrayList;
import java.util.List;

public class CerRequestInfoMapper implements MapperInterface<CerRequestInfo, CerRequestInfoDTO> {
    @Override
    public CerRequestInfo toEntity(CerRequestInfoDTO dto) {
        return null;
    }

    @Override
    public CerRequestInfoDTO toDto(CerRequestInfo entity) {
        return new CerRequestInfoDTO(entity.getId(), entity.getCommonName(), entity.getSurname(), entity.getGivenName(), entity.getOrganization(), entity.getOrganizationUnit(), entity.getCountry(), String.valueOf(entity.getUserId()), entity.getEmail());
    }

    public List<CerRequestInfoDTO> toDTOList(List<CerRequestInfo> categories) {
        ArrayList<CerRequestInfoDTO> dtos = new ArrayList<>();
        for (CerRequestInfo category : categories) {
            CerRequestInfoDTO dto = toDto(category);
            dtos.add(dto);
        }
        return dtos;
    }

}
