package com.hotel.system.service.cliente;

import com.hotel.system.domain.dto.response.ClienteResponse;
import com.hotel.system.domain.entity.Cliente;
import com.hotel.system.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;

    public ClienteResponse registrarCliente(ClienteRequest request) {
        // Verificar si el DNI ya existe
        if (clienteRepository.findByDni(request.getDni()).isPresent()) {
            throw new BusinessException("Ya existe un cliente con el DNI: " + request.getDni());
        }

        Cliente cliente = clienteMapper.toEntity(request);
        cliente = clienteRepository.save(cliente);
        return clienteMapper.toResponse(cliente);
    }

    @Transactional(readOnly = true)
    public ClienteResponse obtenerCliente(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Cliente no encontrado con ID: " + id));
        return clienteMapper.toResponse(cliente);
    }

    @Transactional(readOnly = true)
    public ClienteResponse buscarPorDni(String dni) {
        Cliente cliente = clienteRepository.findByDni(dni)
                .orElseThrow(() -> new BusinessException("Cliente no encontrado con DNI: " + dni));
        return clienteMapper.toResponse(cliente);
    }

    @Transactional(readOnly = true)
    public Page<ClienteResponse> listarClientes(Pageable pageable) {
        return clienteRepository.findAll(pageable)
                .map(clienteMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<ClienteResponse> buscarPorNombre(String nombre) {
        return clienteRepository.buscarPorNombre(nombre).stream()
                .map(clienteMapper::toResponse)
                .collect(Collectors.toList());
    }

    public ClienteResponse actualizarCliente(Long id, ClienteRequest request) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Cliente no encontrado con ID: " + id));

        // Verificar DNI único (si cambió)
        if (!cliente.getDni().equals(request.getDni())) {
            if (clienteRepository.findByDni(request.getDni()).isPresent()) {
                throw new BusinessException("Ya existe un cliente con el DNI: " + request.getDni());
            }
        }

        clienteMapper.updateEntity(cliente, request);
        cliente = clienteRepository.save(cliente);
        return clienteMapper.toResponse(cliente);
    }

    public ClienteResponse marcarProblematico(Long id, String motivo) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Cliente no encontrado con ID: " + id));

        cliente.setEsProblematico(true);
        cliente.setMotivoProblema(motivo);
        cliente = clienteRepository.save(cliente);
        return clienteMapper.toResponse(cliente);
    }

    public ClienteResponse desmarcarProblematico(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Cliente no encontrado con ID: " + id));

        cliente.setEsProblematico(false);
        cliente.setMotivoProblema(null);
        cliente = clienteRepository.save(cliente);
        return clienteMapper.toResponse(cliente);
    }

    @Transactional(readOnly = true)
    public Cliente obtenerClienteEntity(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Cliente no encontrado con ID: " + id));
    }
}