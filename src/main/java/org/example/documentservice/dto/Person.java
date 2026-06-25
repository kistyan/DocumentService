package org.example.documentservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.example.documentservice.enums.Gender;

public record Person(
    @NotBlank(message = "Отсутствует имя")
    String firstName,

    @NotBlank(message = "Отсутствует фамилия")
    String lastName,

    String patronymic,

    @NotNull(message = "Отсутствует пол")
    Gender gender
) {
}
