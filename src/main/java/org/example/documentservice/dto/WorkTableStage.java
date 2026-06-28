package org.example.documentservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

import java.util.List;

@Builder
public record WorkTableStage(
    @NotEmpty(message = "Отсутствуют работы в этапе таблицы работ")
    List<@Valid WorkTableRow> rows
) {
}
