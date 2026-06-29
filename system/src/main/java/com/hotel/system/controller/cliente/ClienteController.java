package com.hotel.system.controller.cliente;

import com.hotel.system.domain.dto.response.ClienteResponse;
import com.hotel.system.service.cliente.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<ClienteResponse> registrarCliente(@Valid @RequestBody ClienteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteService.registrarCliente(request));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<Page<ClienteResponse>> listarClientes(
            @PageableDefault(size = 20, sort = "nombreCompleto", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(clienteService.listarClientes(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<ClienteResponse> obtenerCliente(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.obtenerCliente(id));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<List<ClienteResponse>> buscarClientes(@RequestParam String termino) {
        // Buscar por DNI o nombre
        try {
            // Intentar buscar por DNI
            ClienteResponse cliente = clienteService.buscarPorDni(termino);
            return ResponseEntity.ok(List.of(cliente));
        } catch (Exception e) {
            // Si no es DNI, buscar por nombre
            return ResponseEntity.ok(clienteService.buscarPorNombre(termino));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<ClienteResponse> actualizarCliente(
            @PathVariable Long id,
            @Valid @RequestBody ClienteRequest request) {
        return ResponseEntity.ok(clienteService.actualizarCliente(id, request));
    }

    @PutMapping("/{id}/problematico")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public ResponseEntity<ClienteResponse> marcarProblematico(
            @PathVariable Long id,
            @RequestParam String motivo) {
        return ResponseEntity.ok(clienteService.marcarProblematico(id, motivo));
    }

    @DeleteMapping("/{id}/problematico")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public ResponseEntity<ClienteResponse> desmarcarProblematico(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.desmarcarProblematico(id));
    }
}