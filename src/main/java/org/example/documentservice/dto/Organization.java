package org.example.documentservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record Organization(
    @NotBlank(message = "Отсутствует имя организации")
    String name,

    @NotNull(message = "Отсутствует ИНН организации")
    Long tin,

    @NotNull(message = "Отсутствуют данные о директоре организации")
    @Valid
    Person director
) {
}
