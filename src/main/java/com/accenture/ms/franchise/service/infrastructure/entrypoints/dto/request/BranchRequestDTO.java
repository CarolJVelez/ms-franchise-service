package com.accenture.ms.franchise.service.infrastructure.entrypoints.dto.request;

import jakarta.validation.constraints.NotBlank;

public record BranchRequestDTO(
        @NotBlank(message = "El nombre de la sucursal es obligatorio.")
        String branchName,
        @NotBlank(message = "El id de la franquicia es obligatorio.")
        String franchiseId
) {
}
