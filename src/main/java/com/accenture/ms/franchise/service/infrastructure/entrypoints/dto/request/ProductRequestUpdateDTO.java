package com.accenture.ms.franchise.service.infrastructure.entrypoints.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductRequestUpdateDTO(
        @NotBlank(message = "El id del producto es obligatorio.")
        String productId,
        @NotBlank(message = "El nombre del producto es obligatorio.")
        String productName,
        @NotBlank(message = "El id de la sucursal es obligatorio.")
        String branchId,
        @NotNull(message = "La cantidad del stock es obligatoria.")
        Integer stock
) {
}
