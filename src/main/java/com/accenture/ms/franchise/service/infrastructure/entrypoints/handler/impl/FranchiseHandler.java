package com.accenture.ms.franchise.service.infrastructure.entrypoints.handler.impl;

import com.accenture.ms.franchise.service.domain.api.IFranchiseServicePort;
import com.accenture.ms.franchise.service.domain.enums.TechnicalMessage;
import com.accenture.ms.franchise.service.domain.exceptions.BusinessException;
import com.accenture.ms.franchise.service.domain.exceptions.TechnicalException;
import com.accenture.ms.franchise.service.infrastructure.entrypoints.dto.request.FranchiseRequestDTO;
import com.accenture.ms.franchise.service.infrastructure.entrypoints.dto.request.FranchiseUpdateRequestDTO;
import com.accenture.ms.franchise.service.infrastructure.entrypoints.dto.response.FranchiseResponseDTO;
import com.accenture.ms.franchise.service.infrastructure.entrypoints.dto.response.ProductBranchResponseDTO;
import com.accenture.ms.franchise.service.infrastructure.entrypoints.handler.IFranchiseHandler;
import com.accenture.ms.franchise.service.infrastructure.entrypoints.mapper.FranchiseRequestMapper;
import com.accenture.ms.franchise.service.infrastructure.entrypoints.mapper.FranchiseResponseMapper;
import com.accenture.ms.franchise.service.infrastructure.entrypoints.mapper.ProductBranchResponseMapper;
import com.accenture.ms.franchise.service.infrastructure.entrypoints.mapper.UpdateFranchiseRequestMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FranchiseHandler implements IFranchiseHandler {

    private final IFranchiseServicePort franchiseServicePort;
    private final FranchiseRequestMapper franchiseRequestMapper;
    private final FranchiseResponseMapper franchiseResponseMapper;
    private final ProductBranchResponseMapper productBranchResponseMapper;
    private final UpdateFranchiseRequestMapper updateFranchiseRequestMapper;

    @Override
    public Mono<FranchiseResponseDTO> saveFranchise(FranchiseRequestDTO franchiseRequestDTO) {
        return Mono.just(franchiseRequestDTO)
                .map(franchiseRequestMapper::toModel)
                .flatMap(franchiseServicePort::saveFranchise)
                .map(franchiseResponseMapper::toResponse)
                .onErrorMap(ex -> {
                    log.error("Error al guardar franquicia", ex);
                    if (ex instanceof BusinessException) {
                        return ex;
                    }
                    return new TechnicalException(ex, TechnicalMessage.INTERNAL_ERROR);
                });
    }

    @Override
    public Mono<List<ProductBranchResponseDTO>> getTopStockProducts(String franchiseId) {
        return franchiseServicePort.getTopStockProducts(franchiseId)
                .map(list -> list.stream()
                        .map(productBranchResponseMapper::toResponse)
                        .toList()
                )
                .onErrorMap(ex -> {
                    log.error("Error al obtener productos con mayor stock", ex);
                    if (ex instanceof BusinessException) return ex;
                    return new TechnicalException(ex, TechnicalMessage.INTERNAL_ERROR);
                });
    }

    @Override
    public Mono<FranchiseResponseDTO> updateFranchiseName(FranchiseUpdateRequestDTO franchiseUpdateRequestDTO) {
        return Mono.just(franchiseUpdateRequestDTO)
                .map(updateFranchiseRequestMapper::toModel)
                .flatMap(franchiseServicePort::updateFranchise)
                .map(franchiseResponseMapper::toResponse)
                .onErrorMap(ex -> {
                    log.error("Error al actualizar franquicia", ex);
                    if (ex instanceof BusinessException) {
                        return ex;
                    }
                    return new TechnicalException(ex, TechnicalMessage.INTERNAL_ERROR);
                });
    }
}
