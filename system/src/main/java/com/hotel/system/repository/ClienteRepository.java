package com.hotel.system.repository;

import com.hotel.system.domain.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByDni(String dni);

    @Query("SELECT c FROM Cliente c WHERE LOWER(c.nombreCompleto) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Cliente> buscarPorNombre(@Param("nombre") String nombre);
}