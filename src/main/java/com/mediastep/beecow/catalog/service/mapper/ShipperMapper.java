package com.mediastep.beecow.catalog.service.mapper;

import com.mediastep.beecow.catalog.domain.*;
import com.mediastep.beecow.catalog.service.dto.ShipperDTO;

import com.mediastep.beecow.catalog.service.dto.SimpleShipperDTO;
import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Shipper and its DTO ShipperDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ShipperMapper {

    @Mapping(source = "country.id", target = "countryId")
    ShipperDTO shipperToShipperDTO(Shipper shipper);

    List<ShipperDTO> shippersToShipperDTOs(List<Shipper> shippers);

    @Mapping(source = "countryId", target = "country")
    Shipper shipperDTOToShipper(ShipperDTO shipperDTO);

    List<Shipper> shipperDTOsToShippers(List<ShipperDTO> shipperDTOs);

    SimpleShipperDTO shipperToSimpleShipperDTO(Shipper shipper);

    default Country countryFromId(Long id) {
        if (id == null) {
            return null;
        }
        Country country = new Country();
        country.setId(id);
        return country;
    }
}
