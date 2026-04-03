package com.accenture.ms.franchise.service.infrastructure.entrypoints.dto.response;

public record BranchResponseDTO(
        String branchId,
        String branchName,
        String franchiseId
) {
}
