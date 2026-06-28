package org.example.documentservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record DocumentReference(
    @NotNull(message = "Отсутствует номер документа")
    String number,

    @NotNull(message = "Отсутствует дата документа")
    LocalDate date
) {
}
