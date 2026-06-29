package com.hotel.system.mapper;

import com.hotel.system.domain.dto.request.EstadiaRequest;
import com.hotel.system.domain.dto.response.EstadiaResponse;
import com.hotel.system.domain.entity.Estadia;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface EstadiaMapper {

    @Mapping(target = "clienteId", source = "cliente.id")
    @Mapping(target = "clienteNombre", source = "cliente.nombreCompleto")
    @Mapping(target = "clienteDni", source = "cliente.dni")
    @Mapping(target = "habitacionId", source = "habitacion.id")
    @Mapping(target = "habitacionNumero", source = "habitacion.numero")
    @Mapping(target = "habitacionTipo", source = "habitacion.tipo")
    @Mapping(target = "turnoId", source = "turno.id")
    @Mapping(target = "turnoTipo", source = "turno.tipoTurno")
    EstadiaResponse toResponse(Estadia estadia);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaHoraInicio", ignore = true)
    @Mapping(target = "fechaHoraFin", ignore = true)
    @Mapping(target = "total", ignore = true)
    @Mapping(target = "estado", constant = "ACTIVA")
    @Mapping(target = "cliente", ignore = true)
    @Mapping(target = "habitacion", ignore = true)
    @Mapping(target = "turno", ignore = true)
    Estadia toEntity(EstadiaRequest request);
}