package com.mediastep.beecow.catalog.service.impl;

import com.mediastep.beecow.catalog.config.Constants;
import com.mediastep.beecow.catalog.dto.PackageCurrencyDTO;
import com.mediastep.beecow.catalog.service.PackageCurrencyService;
import com.mediastep.beecow.catalog.domain.PackageCurrency;
import com.mediastep.beecow.catalog.repository.PackageCurrencyRepository;
import com.mediastep.beecow.catalog.service.mapper.PackageCurrencyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link PackageCurrency}.
 */
@Service
@Transactional
public class PackageCurrencyServiceImpl implements PackageCurrencyService {

    private final Logger log = LoggerFactory.getLogger(PackageCurrencyServiceImpl.class);

    private final PackageCurrencyRepository packageCurrencyRepository;

    private final PackageCurrencyMapper packageCurrencyMapper;

    public PackageCurrencyServiceImpl(PackageCurrencyRepository packageCurrencyRepository, PackageCurrencyMapper packageCurrencyMapper) {
        this.packageCurrencyRepository = packageCurrencyRepository;
        this.packageCurrencyMapper = packageCurrencyMapper;
    }

    @Override
    public PackageCurrencyDTO save(PackageCurrencyDTO packageCurrencyDTO) {
        log.debug("Request to save PackageCurrency : {}", packageCurrencyDTO);
        PackageCurrency packageCurrency = packageCurrencyMapper.toEntity(packageCurrencyDTO);
        packageCurrency = packageCurrencyRepository.save(packageCurrency);
        return packageCurrencyMapper.toDto(packageCurrency);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PackageCurrencyDTO> findAll() {
        log.debug("Request to get all PackageCurrencies");
        return packageCurrencyRepository.findAll().stream()
            .map(packageCurrencyMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<PackageCurrencyDTO> findOne(Long id) {
        log.debug("Request to get PackageCurrency : {}", id);
        return packageCurrencyRepository.findById(id)
            .map(packageCurrencyMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PackageCurrency : {}", id);
        packageCurrencyRepository.deleteById(id);
    }

	@Override
    @Cacheable(Constants.CacheName.CATALOG_PACKAGE_CURRENCY_BY_CURRENCY_CODE)
    public PackageCurrencyDTO getPackageCurrencyByCurrencyCode(String currencyCode) {
        switch (currencyCode) {
            case "VND":
                currencyCode = "VND";
                break;
            default:
                currencyCode = "USD";
        }

        return packageCurrencyMapper.toDto(packageCurrencyRepository.findByCurrencyCode(currencyCode));
	}

    @Override
    @Cacheable(Constants.CacheName.CATALOG_PACKAGE_CURRENCY_BY_LOCALE)
    public PackageCurrencyDTO getPackageCurrencyByLocale(String locale) {
        String currencyCode;

        switch (locale) {
            case "vi":
                currencyCode = "VND";
                break;
            default:
                currencyCode = "USD";
        }

        return packageCurrencyMapper.toDto(packageCurrencyRepository.findByCurrencyCode(currencyCode));
    }
}
