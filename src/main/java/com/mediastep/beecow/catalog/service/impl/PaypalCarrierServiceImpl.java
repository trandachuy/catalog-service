package com.mediastep.beecow.catalog.service.impl;

import com.mediastep.beecow.catalog.service.PaypalCarrierService;
import com.mediastep.beecow.catalog.domain.PaypalCarrier;
import com.mediastep.beecow.catalog.repository.PaypalCarrierRepository;
import com.mediastep.beecow.catalog.service.dto.PaypalCarrierDTO;
import com.mediastep.beecow.catalog.service.dto.PaypalCarrierResponse;
import com.mediastep.beecow.catalog.service.mapper.PaypalCarrierMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link PaypalCarrier}.
 */
@Service
@Transactional
public class PaypalCarrierServiceImpl implements PaypalCarrierService {

    private final Logger log = LoggerFactory.getLogger(PaypalCarrierServiceImpl.class);

    private final PaypalCarrierRepository paypalCarrierRepository;

    private final PaypalCarrierMapper paypalCarrierMapper;

    public PaypalCarrierServiceImpl(PaypalCarrierRepository paypalCarrierRepository, PaypalCarrierMapper paypalCarrierMapper) {
        this.paypalCarrierRepository = paypalCarrierRepository;
        this.paypalCarrierMapper = paypalCarrierMapper;
    }

    @Override
    public PaypalCarrierDTO save(PaypalCarrierDTO paypalCarrierDTO) {
        log.debug("Request to save PaypalCarrier : {}", paypalCarrierDTO);
        PaypalCarrier paypalCarrier = paypalCarrierMapper.toEntity(paypalCarrierDTO);
        paypalCarrier = paypalCarrierRepository.save(paypalCarrier);
        return paypalCarrierMapper.toDto(paypalCarrier);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaypalCarrierDTO> findAll() {
        log.debug("Request to get all PaypalCarriers");
        return paypalCarrierRepository.findAll().stream()
            .map(paypalCarrierMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<PaypalCarrierDTO> findOne(Long id) {
        log.debug("Request to get PaypalCarrier : {}", id);
        return paypalCarrierRepository.findById(id)
            .map(paypalCarrierMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PaypalCarrier : {}", id);
        paypalCarrierRepository.deleteById(id);
    }

    @Override
    public List<PaypalCarrierResponse> searchPaypalCarrier() {
        List<PaypalCarrierResponse> res = new ArrayList<>();
        Map<String, List<PaypalCarrierDTO>> mapCarrier = this.findAll().stream().collect(Collectors.groupingBy(PaypalCarrierDTO::getCountryName));
        for (String key : mapCarrier.keySet()){
            res.add(PaypalCarrierResponse.builder()
                    .countryName(key)
                    .paypalCarrierList(mapCarrier.get(key))
                    .build());
        }
        return res;
    }
}
