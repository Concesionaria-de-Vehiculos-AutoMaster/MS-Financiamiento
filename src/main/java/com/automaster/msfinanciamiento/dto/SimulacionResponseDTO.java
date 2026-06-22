package com.automaster.msfinanciamiento.dto;

import lombok.Data;

@Data
public class SimulacionResponseDTO {
    private Long idSimulacion;
    private String rutCliente;
    private Double precioTotalVehiculo;
    private Double saldoAFinanciar;
    private Integer cantidadCuotas;
    private Double valorCuotaMensual;
}