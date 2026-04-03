package com.accenture.ms.franchise.service.infrastructure.entrypoints.dto.request;

import jakarta.validation.constraints.NotBlank;

public record FranchiseRequestDTO(
        @NotBlank(message = "El nombre de la franquicia es obligatorio.")
        String franchiseName
) {
}
