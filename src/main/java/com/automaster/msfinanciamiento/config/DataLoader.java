package com.automaster.msfinanciamiento.config;

import com.automaster.msfinanciamiento.model.Simulacion;
import com.automaster.msfinanciamiento.repository.SimulacionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Locale;

@Slf4j
@Profile("test")
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final SimulacionRepository simulacionRepository;

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker(new Locale("es"));

        if (simulacionRepository.count() == 0) {
            log.info("Iniciando la carga de datos ficticios para Financiamiento (Simulaciones)...");

            for (int i = 0; i < 10; i++) {
                Simulacion simulacion = new Simulacion();

                String rutAleatorio = faker.number().numberBetween(10000000, 25000000) + "-" + faker.number().numberBetween(0, 9);
                simulacion.setRutCliente(rutAleatorio);
                simulacion.setIdVehiculo(faker.number().numberBetween(1L, 50L));
                double precio = faker.number().randomDouble(2, 8500000, 28000000); // Precios entre 8.5M y 28M
                double pie = precio * 0.20;
                int cuotas = faker.options().option(12, 24, 36, 48, 60);
                double valorCuota = (precio - pie) / cuotas;

                simulacion.setPrecioVehiculo(precio);
                simulacion.setPieAbonado(pie);
                simulacion.setCantidadCuotas(cuotas);
                simulacion.setValorCuota(valorCuota);
                simulacion.setFechaSimulacion(LocalDateTime.now().minusDays(faker.number().numberBetween(0, 30)));

                simulacionRepository.save(simulacion);
            }

            log.info("¡Carga de {} simulaciones de financiamiento completada con éxito!", simulacionRepository.count());
        } else {
            log.info("La tabla de simulaciones ya contiene datos. Se omitió la inicialización.");
        }
    }
}