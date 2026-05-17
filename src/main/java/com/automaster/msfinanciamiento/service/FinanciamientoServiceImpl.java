package com.automaster.msfinanciamiento.service;

import com.automaster.msfinanciamiento.dto.SimulacionRequestDTO;
import com.automaster.msfinanciamiento.dto.SimulacionResponseDTO;
import com.automaster.msfinanciamiento.model.Simulacion;
import com.automaster.msfinanciamiento.repository.SimulacionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Service
public class FinanciamientoServiceImpl {

    @Autowired
    private SimulacionRepository simulacionRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    // Tasa de interés fija del 3% mensual para el ejemplo
    private static final Double TASA_INTERES_MENSUAL = 0.03;

    public SimulacionResponseDTO simularCredito(SimulacionRequestDTO request) {
        log.info("Iniciando simulación de crédito para cliente {} y vehículo {}", request.getRutCliente(), request.getIdVehiculo());

        Double precioVehiculo = obtenerPrecioVehiculoDesdeStock(request.getIdVehiculo());

        if (request.getPieAbonado() >= precioVehiculo) {
            log.error("El pie abonado ({}) es mayor o igual al precio del vehículo ({})", request.getPieAbonado(), precioVehiculo);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El pie no puede ser mayor o igual al precio del vehículo");
        }

        // Matemáticas del crédito
        Double saldoAFinanciar = precioVehiculo - request.getPieAbonado();
        Double valorCuota = (saldoAFinanciar / request.getCantidadCuotas()) * (1 + TASA_INTERES_MENSUAL);

        // Guardar registro en BD (JPA)
        Simulacion simulacion = new Simulacion();
        simulacion.setRutCliente(request.getRutCliente());
        simulacion.setIdVehiculo(request.getIdVehiculo());
        simulacion.setPrecioVehiculo(precioVehiculo);
        simulacion.setPieAbonado(request.getPieAbonado());
        simulacion.setCantidadCuotas(request.getCantidadCuotas());
        simulacion.setValorCuota(valorCuota);
        simulacion.setFechaSimulacion(LocalDateTime.now());

        Simulacion guardada = simulacionRepository.save(simulacion);
        log.info("Simulación guardada exitosamente con ID: {}", guardada.getId());

        return mapearADTO(guardada, saldoAFinanciar);
    }

    // USO OBLIGATORIO DE WEBCLIENT SEGÚN RÚBRICA
    private Double obtenerPrecioVehiculoDesdeStock(Long idVehiculo) {
        log.info("Consultando precio del vehículo ID {} en MS-Stock mediante WebClient", idVehiculo);
        try {
            // Asumimos que MS-Stock está en el puerto 8080. Cambia la URL si es distinta.
            Map respuesta = webClientBuilder.build()
                    .get()
                    .uri("http://localhost:8080/api/stock/" + idVehiculo)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (respuesta != null && respuesta.containsKey("precioVenta")) {
                return Double.valueOf(respuesta.get("precioVenta").toString());
            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo obtener el precio del vehículo");
            }

        } catch (WebClientResponseException.NotFound ex) {
            log.error("El vehículo ID {} no existe en MS-Stock", idVehiculo);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El vehículo seleccionado no existe en el inventario");
        } catch (Exception ex) {
            log.error("Error de comunicación con MS-Stock: {}", ex.getMessage());
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Servicio de inventario no disponible");
        }
    }

    private SimulacionResponseDTO mapearADTO(Simulacion simulacion, Double saldoAFinanciar) {
        SimulacionResponseDTO dto = new SimulacionResponseDTO();
        dto.setIdSimulacion(simulacion.getId());
        dto.setRutCliente(simulacion.getRutCliente());
        dto.setPrecioTotalVehiculo(simulacion.getPrecioVehiculo());
        dto.setSaldoAFinanciar(saldoAFinanciar);
        dto.setCantidadCuotas(simulacion.getCantidadCuotas());
        dto.setValorCuotaMensual(simulacion.getValorCuota());
        return dto;
    }
}