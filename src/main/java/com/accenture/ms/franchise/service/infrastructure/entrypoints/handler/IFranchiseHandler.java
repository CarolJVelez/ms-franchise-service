package com.accenture.ms.franchise.service.infrastructure.entrypoints.handler;

import com.accenture.ms.franchise.service.infrastructure.entrypoints.dto.request.FranchiseRequestDTO;
import com.accenture.ms.franchise.service.infrastructure.entrypoints.dto.request.FranchiseUpdateRequestDTO;
import com.accenture.ms.franchise.service.infrastructure.entrypoints.dto.response.FranchiseResponseDTO;
import com.accenture.ms.franchise.service.infrastructure.entrypoints.dto.response.ProductBranchResponseDTO;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IFranchiseHandler {
    Mono<FranchiseResponseDTO> saveFranchise(FranchiseRequestDTO franchiseRequestDTO);
    Mono<List<ProductBranchResponseDTO>> getTopStockProducts(String franchiseId);

    Mono<FranchiseResponseDTO> updateFranchiseName(FranchiseUpdateRequestDTO franchiseUpdateRequestDTO);
}
