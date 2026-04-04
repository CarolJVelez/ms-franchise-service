package com.accenture.ms.franchise.service.infrastructure.entrypoints.dto.response;

public record ProductResponseDTO(
        String productId,
        String productName,
        Integer stock,
        String branchId
) {
}
