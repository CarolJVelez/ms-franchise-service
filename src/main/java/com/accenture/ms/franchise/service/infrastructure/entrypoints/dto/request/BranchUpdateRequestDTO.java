package com.accenture.ms.franchise.service.infrastructure.entrypoints.dto.request;

import jakarta.validation.constraints.NotBlank;

public record BranchUpdateRequestDTO(
        @NotBlank(message = "El id de la sucursal es obligatorio.")
        String branchId,
        @NotBlank(message = "El nombre de la sucursal es obligatorio.")
        String branchName
) {
}
