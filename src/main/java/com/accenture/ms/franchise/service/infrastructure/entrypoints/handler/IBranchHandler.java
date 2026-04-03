package com.accenture.ms.franchise.service.infrastructure.entrypoints.handler;

import com.accenture.ms.franchise.service.infrastructure.entrypoints.dto.request.BranchRequestDTO;
import com.accenture.ms.franchise.service.infrastructure.entrypoints.dto.response.BranchResponseDTO;
import reactor.core.publisher.Mono;

public interface IBranchHandler {
    Mono<BranchResponseDTO> saveBranch(BranchRequestDTO branchRequestDTO);
}
