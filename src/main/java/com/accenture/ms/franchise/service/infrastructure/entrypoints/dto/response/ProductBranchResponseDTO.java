package com.accenture.ms.franchise.service.infrastructure.entrypoints.dto.response;

public record ProductBranchResponseDTO(
        String productId,
        String productName,
        Integer stock,
        String branchId,
        String branchName,
        String franchiseId,
        String franchiseName
) {
}
