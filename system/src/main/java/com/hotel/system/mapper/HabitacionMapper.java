package com.hotel.system.mapper;

import com.hotel.system.domain.dto.request.HabitacionRequest;
import com.hotel.system.domain.dto.response.HabitacionResponse;
import com.hotel.system.domain.entity.Habitacion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface HabitacionMapper {

    HabitacionResponse toResponse(Habitacion habitacion);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "estado", constant = "DISPONIBLE")
    @Mapping(target = "fechaCreacion", ignore = true)
    Habitacion toEntity(HabitacionRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    void updateEntity(@MappingTarget Habitacion habitacion, HabitacionRequest request);
}