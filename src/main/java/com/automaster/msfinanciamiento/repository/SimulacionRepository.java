package com.automaster.msfinanciamiento.repository;

import com.automaster.msfinanciamiento.model.Simulacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SimulacionRepository extends JpaRepository<Simulacion, Long> {
}