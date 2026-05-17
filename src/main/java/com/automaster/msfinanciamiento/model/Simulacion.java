package com.automaster.msfinanciamiento.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "simulaciones")
@Data
public class Simulacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String rutCliente;

    @Column(nullable = false)
    private Long idVehiculo;

    @Column(nullable = false)
    private Double precioVehiculo;

    @Column(nullable = false)
    private Double pieAbonado;

    @Column(nullable = false)
    private Integer cantidadCuotas;

    @Column(nullable = false)
    private Double valorCuota;

    @Column(nullable = false)
    private LocalDateTime fechaSimulacion;
}