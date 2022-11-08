package com.mediastep.beecow.catalog.service.mapper;


import com.mediastep.beecow.catalog.domain.BankInfo;
import com.mediastep.beecow.catalog.dto.BankInfoDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link BankInfo} and its DTO {@link BankInfoDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BankInfoMapper extends EntityMapper<BankInfoDTO, BankInfo> {



    default BankInfo fromId(Long id) {
        if (id == null) {
            return null;
        }
        BankInfo bankInfo = new BankInfo();
        bankInfo.setId(id);
        return bankInfo;
    }
}
