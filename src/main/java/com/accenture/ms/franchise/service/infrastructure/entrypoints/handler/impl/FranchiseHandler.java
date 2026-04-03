package com.accenture.ms.franchise.service.infrastructure.entrypoints.handler.impl;

import com.accenture.ms.franchise.service.domain.api.IFranchiseServicePort;
import com.accenture.ms.franchise.service.domain.enums.TechnicalMessage;
import com.accenture.ms.franchise.service.domain.exceptions.BusinessException;
import com.accenture.ms.franchise.service.domain.exceptions.TechnicalException;
import com.accenture.ms.franchise.service.infrastructure.entrypoints.dto.request.FranchiseRequestDTO;
import com.accenture.ms.franchise.service.infrastructure.entrypoints.dto.response.FranchiseResponseDTO;
import com.accenture.ms.franchise.service.infrastructure.entrypoints.handler.IFranchiseHandler;
import com.accenture.ms.franchise.service.infrastructure.entrypoints.mapper.FranchiseRequestMapper;
import com.accenture.ms.franchise.service.infrastructure.entrypoints.mapper.FranchiseResponseMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class FranchiseHandler implements IFranchiseHandler {

    private final IFranchiseServicePort franchiseServicePort;
    private final FranchiseRequestMapper franchiseRequestMapper;
    private final FranchiseResponseMapper franchiseResponseMapper;

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
}
