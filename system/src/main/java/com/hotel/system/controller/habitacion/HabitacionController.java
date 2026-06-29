package com.hotel.system.controller.habitacion;

import com.hotel.system.domain.dto.response.HabitacionResponse;
import com.hotel.system.domain.enums.EstadoHabitacion;
import com.hotel.system.domain.enums.TipoHabitacion;
import com.hotel.system.service.habitacion.HabitacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/habitaciones")
@RequiredArgsConstructor
public class HabitacionController {

    private final HabitacionService habitacionService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public ResponseEntity<HabitacionResponse> crearHabitacion(@Valid @RequestBody HabitacionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(habitacionService.crearHabitacion(request));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA', 'GERENTE')")
    public ResponseEntity<List<HabitacionResponse>> listarTodas() {
        return ResponseEntity.ok(habitacionService.listarTodas());
    }

    @GetMapping("/disponibles")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<List<HabitacionResponse>> listarDisponibles() {
        return ResponseEntity.ok(habitacionService.listarDisponibles());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA', 'GERENTE')")
    public ResponseEntity<HabitacionResponse> obtenerHabitacion(@PathVariable Long id) {
        return ResponseEntity.ok(habitacionService.obtenerHabitacion(id));
    }

    @GetMapping("/tipo/{tipo}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<List<HabitacionResponse>> listarPorTipo(@PathVariable TipoHabitacion tipo) {
        return ResponseEntity.ok(habitacionService.listarPorTipo(tipo));
    }

    @GetMapping("/disponibles/tipo/{tipo}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<List<HabitacionResponse>> listarDisponiblesPorTipo(@PathVariable TipoHabitacion tipo) {
        return ResponseEntity.ok(habitacionService.listarDisponiblesPorTipo(tipo));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public ResponseEntity<HabitacionResponse> actualizarHabitacion(
            @PathVariable Long id,
            @Valid @RequestBody HabitacionRequest request) {
        return ResponseEntity.ok(habitacionService.actualizarHabitacion(id, request));
    }

    @PutMapping("/{id}/estado")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA', 'MANTENIMIENTO')")
    public ResponseEntity<HabitacionResponse> actualizarEstado(
            @PathVariable Long id,
            @RequestParam EstadoHabitacion estado) {
        return ResponseEntity.ok(habitacionService.actualizarEstado(id, estado));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarHabitacion(@PathVariable Long id) {
        habitacionService.eliminarHabitacion(id);
        return ResponseEntity.noContent().build();
    }
}