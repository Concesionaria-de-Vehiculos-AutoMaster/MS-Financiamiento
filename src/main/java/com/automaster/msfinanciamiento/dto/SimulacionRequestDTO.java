package com.automaster.msfinanciamiento.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SimulacionRequestDTO {
    @NotBlank(message = "El RUT del cliente es obligatorio")
    private String rutCliente;

    @NotNull(message = "El ID del vehículo es obligatorio")
    private Long idVehiculo;

    @NotNull(message = "El pie inicial es obligatorio")
    @Min(value = 0, message = "El pie no puede ser negativo")
    private Double pieAbonado;

    @NotNull(message = "La cantidad de cuotas es obligatoria")
    @Min(value = 1, message = "Debe ser al menos 1 cuota")
    private Integer cantidadCuotas;
}