package com.mediastep.beecow.catalog.service.impl;

import com.mediastep.beecow.catalog.domain.BankInfo;
import com.mediastep.beecow.catalog.dto.BankInfoDTO;
import com.mediastep.beecow.catalog.repository.BankInfoRepository;
import com.mediastep.beecow.catalog.service.BankInfoService;
import com.mediastep.beecow.catalog.service.mapper.BankInfoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link BankInfo}.
 */
@Service
@Transactional
public class BankInfoServiceImpl implements BankInfoService {

    private final Logger log = LoggerFactory.getLogger(BankInfoServiceImpl.class);

    private final BankInfoRepository bankInfoRepository;

    private final BankInfoMapper bankInfoMapper;

    public BankInfoServiceImpl(BankInfoRepository bankInfoRepository, BankInfoMapper bankInfoMapper) {
        this.bankInfoRepository = bankInfoRepository;
        this.bankInfoMapper = bankInfoMapper;
    }

    @Deprecated
    public BankInfoDTO save(BankInfoDTO bankInfoDTO) {
        log.debug("Request to save BankInfo : {}", bankInfoDTO);
        BankInfo bankInfo = bankInfoMapper.toEntity(bankInfoDTO);
        bankInfo = bankInfoRepository.save(bankInfo);
        BankInfoDTO result = bankInfoMapper.toDto(bankInfo);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BankInfoDTO> findAll() {
        log.debug("Request to get all BankInfos");
        return bankInfoRepository.findAll().stream()
            .map(bankInfoMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, String> findAllBank() {
        log.debug("Request to get all BankInfos");
        return bankInfoRepository.findAll().parallelStream()
                .collect(Collectors.toMap(t -> t.getId().toString(), t -> t.getBankName(), (t1, t2) -> t1));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BankInfoDTO> findOne(Long id) {
        log.debug("Request to get BankInfo : {}", id);
        return bankInfoRepository.findFirstById(id)
            .map(bankInfoMapper::toDto);
    }

    @Deprecated
    public void delete(Long id) {
        log.debug("Request to delete BankInfo : {}", id);
        bankInfoRepository.deleteById(id);
    }

}
