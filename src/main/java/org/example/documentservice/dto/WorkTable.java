package org.example.documentservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record WorkTable(
    @NotEmpty(message = "Отсутствуют этапы таблицы работ")
    List<@Valid WorkTableStage> stages
) {
}
