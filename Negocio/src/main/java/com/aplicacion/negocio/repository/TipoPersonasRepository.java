package com.aplicacion.negocio.repository;

import com.aplicacion.negocio.entity.Tipo_Personas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author XPC
 */
@Repository
public interface TipoPersonasRepository extends JpaRepository<Tipo_Personas, Integer> {

}
