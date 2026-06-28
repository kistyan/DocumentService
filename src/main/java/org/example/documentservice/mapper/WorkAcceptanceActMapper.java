package org.example.documentservice.mapper;

import org.mapstruct.Mapper;
import org.example.documentservice.dto.response.WorkAcceptanceActResponse;
import org.example.documentservice.model.WorkAcceptanceAct;

@Mapper(componentModel = "spring")
public interface WorkAcceptanceActMapper {
  WorkAcceptanceActResponse toResponse(WorkAcceptanceAct entity);
}
