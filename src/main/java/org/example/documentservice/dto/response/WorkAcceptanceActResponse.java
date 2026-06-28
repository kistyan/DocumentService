package org.example.documentservice.dto.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record WorkAcceptanceActResponse(
    UUID id,

    String number
) {
}
