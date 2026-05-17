package com.automaster.msfinanciamiento.controller;

import com.automaster.msfinanciamiento.dto.SimulacionRequestDTO;
import com.automaster.msfinanciamiento.dto.SimulacionResponseDTO;
import com.automaster.msfinanciamiento.service.FinanciamientoServiceImpl;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/financiamiento")
public class FinanciamientoController {

    @Autowired
    private FinanciamientoServiceImpl financiamientoService;

    @PostMapping("/simular")
    public ResponseEntity<SimulacionResponseDTO> simularCredito(@Valid @RequestBody SimulacionRequestDTO request) {
        log.info("Petición REST POST recibida para simular crédito");
        SimulacionResponseDTO response = financiamientoService.simularCredito(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}