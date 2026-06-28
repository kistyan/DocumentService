package org.example.documentservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.example.documentservice.enums.Gender;

@Builder
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
