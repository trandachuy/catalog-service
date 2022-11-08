/*
 * Copyright 2017 (C) Mediastep Software Inc.
 *
 * Created on : 4/3/19
 * Author: Quy Luong <email:quy.luong@mediastep.com>
 *
 */
package com.mediastep.beecow.catalog.client;

import com.mediastep.beecow.store.service.dto.StoreDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "storeService")
public interface StoreCatalogServiceClient {
    @RequestMapping(value = "/api/stores/{id}", method = RequestMethod.GET)
    ResponseEntity<StoreDTO> getStoreById(@RequestHeader("Authorization") String jwtToken, @PathVariable("id") Long id);
}
