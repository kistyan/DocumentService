package org.example.documentservice.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.example.documentservice.dto.DocumentReference;
import org.example.documentservice.dto.OrganizationInfo;
import org.example.documentservice.dto.WorkTable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record WorkAcceptanceActRequest(
    @NotNull(message = "Отсутствуют данные о договоре")
    @Valid
    DocumentReference contract,

    @NotNull(message = "Отсутствуют данные о заявке")
    @Valid
    DocumentReference request,

    @NotNull(message = "Отсутствуют данные о заказчике")
    @Valid
    OrganizationInfo customer,

    @NotNull(message = "Отсутствуют данные об исполнителе")
    @Valid
    OrganizationInfo contractor,

    @NotBlank(message = "Отсутствует название продукта")
    String product,

    @NotEmpty(message = "Отсутствуют выполненные работы")
    List<String> works,

    @NotNull(message = "Отсутствует дата начала выполнения работ")
    LocalDate startDate,

    @NotNull(message = "Отсутствует дата окончания выполнения работ")
    LocalDate endDate,

    @NotNull(message = "Отсутствует таблица работ")
    @Valid
    WorkTable workTable,

    @NotNull(message = "Отсутствует НДС")
    @PositiveOrZero(message = "НДС не может быть отрицательным")
    BigDecimal vatPercent
) {
}
