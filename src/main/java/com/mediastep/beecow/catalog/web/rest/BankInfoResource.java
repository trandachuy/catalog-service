package com.mediastep.beecow.catalog.web.rest;

import com.mediastep.beecow.catalog.dto.BankInfoDTO;
import com.mediastep.beecow.catalog.service.BankInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST controller for managing {@link com.mediastep.beecow.catalog.domain.BankInfo}.
 */
@RestController
@RequestMapping("/api")
public class BankInfoResource {

    private final Logger log = LoggerFactory.getLogger(BankInfoResource.class);

    private static final String ENTITY_NAME = "catalogServicesBankInfo";

    private final BankInfoService bankInfoService;

    public BankInfoResource(BankInfoService bankInfoService) {
        this.bankInfoService = bankInfoService;
    }

    /**
     * {@code GET  /bank-infos} : get all the bankInfos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bankInfos in body.
     */
    @GetMapping("/bank-infos")
    public List<BankInfoDTO> getAllBankInfos() {
        log.debug("REST request to get all BankInfos");
        return bankInfoService.findAll();
    }

    /**
     * {@code GET  /bank-infos} : get all the bankInfos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bankInfos in body.
     */
    @GetMapping("/bank-infos/map")
    public Map<String, String> findAllBankInfos() {
        log.debug("REST request to find all BankInfos");
        return bankInfoService.findAllBank();
    }

    /**
     * {@code GET  /bank-infos/:id} : get the "id" bankInfo.
     *
     * @param id the id of the bankInfoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bankInfoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bank-infos/{id}")
    public ResponseEntity<BankInfoDTO> getBankInfo(@PathVariable Long id) {
        log.debug("REST request to get BankInfo : {}", id);
        Optional<BankInfoDTO> bankInfoDTO = bankInfoService.findOne(id);
        return ResponseEntity.ok().body(bankInfoDTO.orElse(null));
    }

}
