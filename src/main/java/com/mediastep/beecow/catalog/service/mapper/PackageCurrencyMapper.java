package com.mediastep.beecow.catalog.service.mapper;


import com.mediastep.beecow.catalog.domain.*;

import com.mediastep.beecow.catalog.dto.PackageCurrencyDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PackageCurrency} and its DTO {@link PackageCurrencyDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PackageCurrencyMapper extends EntityMapper<PackageCurrencyDTO, PackageCurrency> {



    default PackageCurrency fromId(Long id) {
        if (id == null) {
            return null;
        }
        PackageCurrency packageCurrency = new PackageCurrency();
        packageCurrency.setId(id);
        return packageCurrency;
    }
}
