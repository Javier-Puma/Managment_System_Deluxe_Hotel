package com.hotel.system.controller.estadia;

import com.hotel.system.domain.dto.request.CheckoutRequest;
import com.hotel.system.domain.dto.request.EstadiaRequest;
import com.hotel.system.domain.dto.response.EstadiaResponse;
import com.hotel.system.service.estadia.EstadiaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/estadias")
@RequiredArgsConstructor
public class EstadiaController {

    private final EstadiaService estadiaService;

    @PostMapping("/checkin")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<EstadiaResponse> realizarCheckIn(@Valid @RequestBody EstadiaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(estadiaService.realizarCheckIn(request));
    }

    @PutMapping("/checkout")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<EstadiaResponse> realizarCheckOut(@Valid @RequestBody CheckoutRequest request) {
        return ResponseEntity.ok(estadiaService.realizarCheckOut(request));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<List<EstadiaResponse>> listarTodas() {
        return ResponseEntity.ok(estadiaService.listarTodas());
    }

    @GetMapping("/activas")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<List<EstadiaResponse>> listarActivas() {
        return ResponseEntity.ok(estadiaService.listarEstadiasActivas());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<EstadiaResponse> obtenerEstadia(@PathVariable Long id) {
        return ResponseEntity.ok(estadiaService.obtenerEstadia(id));
    }

    @GetMapping("/cliente/{clienteId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<List<EstadiaResponse>> listarHistorialCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(estadiaService.listarHistorialCliente(clienteId));
    }
}