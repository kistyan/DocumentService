package org.example.documentservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

import java.util.List;

@Builder
public record WorkTable(
    @NotEmpty(message = "Отсутствуют этапы таблицы работ")
    List<@Valid WorkTableStage> stages
) {
}
