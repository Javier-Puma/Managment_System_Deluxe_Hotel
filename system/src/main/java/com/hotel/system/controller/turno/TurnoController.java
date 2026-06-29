package com.hotel.system.controller.turno;

import com.hotel.system.domain.dto.request.AbrirTurnoRequest;
import com.hotel.system.domain.dto.response.TurnoResponse;
import com.hotel.system.service.turno.TurnoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/turnos")
@RequiredArgsConstructor
public class TurnoController {

    private final TurnoService turnoService;

    @PostMapping("/abrir")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<TurnoResponse> abrirTurno(@Valid @RequestBody AbrirTurnoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(turnoService.abrirTurno(request));
    }

    @PutMapping("/{id}/cerrar")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<TurnoResponse> cerrarTurno(@PathVariable Long id) {
        return ResponseEntity.ok(turnoService.cerrarTurno(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public ResponseEntity<List<TurnoResponse>> listarTurnos() {
        return ResponseEntity.ok(turnoService.listarTurnos());
    }

    @GetMapping("/activo")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA', 'GERENTE')")
    public ResponseEntity<TurnoResponse> obtenerTurnoActivo() {
        return ResponseEntity.ok(turnoService.obtenerTurnoActivo());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public ResponseEntity<TurnoResponse> obtenerTurno(@PathVariable Long id) {
        return ResponseEntity.ok(turnoService.obtenerTurno(id));
    }

    @GetMapping("/empleado/{empleadoId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public ResponseEntity<List<TurnoResponse>> listarTurnosPorEmpleado(@PathVariable Long empleadoId) {
        return ResponseEntity.ok(turnoService.listarTurnosPorEmpleado(empleadoId));
    }
}