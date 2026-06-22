package com.automaster.msfinanciamiento.controller;

import com.automaster.msfinanciamiento.dto.SimulacionRequestDTO;
import com.automaster.msfinanciamiento.dto.SimulacionResponseDTO;
import com.automaster.msfinanciamiento.service.FinanciamientoServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/financiamiento")
@Tag(name= "Financiamiento", description = "Operaciones relacionadas a Financiamiento")
public class FinanciamientoController {

    @Autowired
    private FinanciamientoServiceImpl financiamientoService;

    @PostMapping("/simular")
    @Operation(summary = "Craer un Financiamiento", description = "Agregas nuevos Financiamientos para la base de datos")
    public ResponseEntity<SimulacionResponseDTO> simularCredito(@Valid @RequestBody SimulacionRequestDTO request) {
        log.info("Petición REST POST recibida para simular un financiamiento");
        SimulacionResponseDTO response = financiamientoService.simularCredito(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar Financiamiento por Id", description = "Elimina cualquier Financiamiento por su Id")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Long id) {
        log.info("Petición DELETE recibida para eliminar Financiamiento ID: {}", id);
        financiamientoService.eliminarFinanciamiento(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar Financiamiento por Id", description = "Encuentra Financiamiento por su Id")
    public ResponseEntity<SimulacionResponseDTO> obtenerPorId(@PathVariable Long id) {
        SimulacionResponseDTO response = financiamientoService.buscarPorId(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "Listar Financiamiento", description = "Muestra una lista con todos los Financiamiento")
    public ResponseEntity<List<SimulacionResponseDTO>> listarFinanciamiento() {
        log.info("Petición GET recibida para listar todos los clientes");
        List<SimulacionResponseDTO> response = financiamientoService.listarTodos();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}