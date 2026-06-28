package org.example.documentservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record WorkTableRow(
    @NotNull(message = "Отсутствует наименование работы")
    String work,

    @NotNull(message = "Отсутствует тариф")
    @PositiveOrZero(message = "Тариф не может быть отрицательным")
    BigDecimal rate,

    @NotNull(message = "Отсутствует количество часов")
    Integer hours,

    @NotNull(message = "Отсутствует дата начала выполнения работы")
    LocalDate startDate,

    @NotNull(message = "Отсутствует дата окончания выполнения работы")
    LocalDate endDate
) {
}
