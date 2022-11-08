package com.mediastep.beecow.catalog.service.impl;

import com.mediastep.beecow.catalog.domain.Shipper;
import com.mediastep.beecow.catalog.repository.ShipperRepository;
import com.mediastep.beecow.catalog.service.ShipperService;
import com.mediastep.beecow.catalog.service.dto.ShipperDTO;
import com.mediastep.beecow.catalog.service.dto.SimpleShipperDTO;
import com.mediastep.beecow.catalog.service.mapper.ShipperMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Shipper.
 */
@Service
@Transactional
public class ShipperServiceImpl implements ShipperService{

    private final Logger log = LoggerFactory.getLogger(ShipperServiceImpl.class);

    @Inject
    private ShipperRepository shipperRepository;

    @Inject
    private ShipperMapper shipperMapper;

    /**
     * Save a shipper.
     *
     * @param shipperDTO the entity to save
     * @return the persisted entity
     */
    public ShipperDTO save(ShipperDTO shipperDTO) {
        log.debug("Request to save Shipper : {}", shipperDTO);
        Shipper shipper = shipperMapper.shipperDTOToShipper(shipperDTO);
        shipper = shipperRepository.save(shipper);
        ShipperDTO result = shipperMapper.shipperToShipperDTO(shipper);
        return result;
    }

    /**
     *  Get all the shippers.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ShipperDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Shippers");
        Page<Shipper> result = shipperRepository.findAll(pageable);
        return result.map(shipper -> shipperMapper.shipperToShipperDTO(shipper));
    }

    /**
     *  Get all the shippers.
     *
     *  @param countryId the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<SimpleShipperDTO> findAll(Long countryId) {
        log.debug("Request to get all Shippers");
        List<SimpleShipperDTO> rs = shipperRepository.findAllByCountryId(countryId).stream()
            .map(shipperMapper::shipperToSimpleShipperDTO).collect(Collectors.toList());

        return rs;
    }

    /**
     *  Get one shipper by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ShipperDTO findOne(Long id) {
        log.debug("Request to get Shipper : {}", id);
        Shipper shipper = shipperRepository.findById(id).orElse(null);
        ShipperDTO shipperDTO = shipperMapper.shipperToShipperDTO(shipper);
        return shipperDTO;
    }

    /**
     *  Delete the  shipper by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Shipper : {}", id);
        shipperRepository.deleteById(id);
    }
}
