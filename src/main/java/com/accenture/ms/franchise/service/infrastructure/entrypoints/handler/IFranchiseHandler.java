package com.accenture.ms.franchise.service.infrastructure.entrypoints.handler;

import com.accenture.ms.franchise.service.infrastructure.entrypoints.dto.request.FranchiseRequestDTO;
import com.accenture.ms.franchise.service.infrastructure.entrypoints.dto.response.FranchiseResponseDTO;
import reactor.core.publisher.Mono;

public interface IFranchiseHandler {
    Mono<FranchiseResponseDTO> saveFranchise(FranchiseRequestDTO franchiseRequestDTO);
}
