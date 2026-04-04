package com.accenture.ms.franchise.service.infrastructure.entrypoints.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ProductRequestUpdateNameDTO(
        @NotBlank(message = "El id del producto es obligatorio.")
        String productId,
        @NotBlank(message = "El nombre del producto es obligatorio.")
        String productName,
        @NotBlank(message = "El id de la sucursal es obligatorio.")
        String branchId
) {
}
