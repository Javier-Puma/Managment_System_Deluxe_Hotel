package com.hotel.system.mapper;

import com.hotel.system.domain.dto.request.ClienteRequest;
import com.hotel.system.domain.dto.response.ClienteResponse;
import com.hotel.system.domain.entity.Cliente;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ClienteMapper {

    ClienteResponse toResponse(Cliente cliente);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaRegistro", ignore = true)
    @Mapping(target = "esProblematico", constant = "false")
    Cliente toEntity(ClienteRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaRegistro", ignore = true)
    @Mapping(target = "esProblematico", ignore = true)
    void updateEntity(@MappingTarget Cliente cliente, ClienteRequest request);
}