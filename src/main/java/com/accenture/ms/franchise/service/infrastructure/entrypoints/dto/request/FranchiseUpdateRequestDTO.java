package com.accenture.ms.franchise.service.infrastructure.entrypoints.dto.request;

import jakarta.validation.constraints.NotBlank;

public record FranchiseUpdateRequestDTO(
        @NotBlank(message = "El id de la franquicia es obligatorio.")
        String franchiseId,
        @NotBlank(message = "El nombre de la franquicia es obligatorio.")
        String franchiseName
) {
}
