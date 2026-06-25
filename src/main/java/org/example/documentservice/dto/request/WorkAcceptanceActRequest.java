package org.example.documentservice.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.example.documentservice.dto.Document;
import org.example.documentservice.dto.Organization;
import org.example.documentservice.dto.WorkTable;

import java.time.LocalDate;
import java.util.List;

public record WorkAcceptanceActRequest(
    @NotNull(message = "Отсутствуют данные о договоре")
    @Valid
    Document contract,

    @NotNull(message = "Отсутствуют данные о заявке")
    @Valid
    Document request,

    @NotNull(message = "Отсутствуют данные о заказчике")
    @Valid
    Organization customer,

    @NotNull(message = "Отсутствуют данные об исполнителе")
    @Valid
    Organization contractor,

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
    WorkTable workTable
) {
}
