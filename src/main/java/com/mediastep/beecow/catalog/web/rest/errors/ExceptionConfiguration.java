package com.mediastep.beecow.catalog.web.rest.errors;

import com.mediastep.beecow.catalog.CatalogServicesApp;
import io.github.jhipster.web.util.HeaderUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/*
 * Copyright 2017 (C) Mediastep Software Inc.
 *
 * Created on : 04/04/2019
 * Author: Quy Luong <email:quy.luong@mediastep.com>
 *
 */
@ControllerAdvice
public class ExceptionConfiguration extends ResponseEntityExceptionHandler {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity argumentTypeMismatch(MethodArgumentTypeMismatchException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .headers(HeaderUtil.createFailureAlert(CatalogServicesApp.class.getSimpleName(), false, "", "wrongParamsType", "wrongParamsType"))
            .body(null);
    }

}
