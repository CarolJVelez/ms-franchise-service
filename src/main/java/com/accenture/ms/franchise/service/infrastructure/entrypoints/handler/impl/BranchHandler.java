package com.accenture.ms.franchise.service.infrastructure.entrypoints.handler.impl;

import com.accenture.ms.franchise.service.domain.api.IFranchiseServicePort;
import com.accenture.ms.franchise.service.domain.enums.TechnicalMessage;
import com.accenture.ms.franchise.service.domain.exceptions.BusinessException;
import com.accenture.ms.franchise.service.domain.exceptions.TechnicalException;
import com.accenture.ms.franchise.service.infrastructure.entrypoints.dto.request.BranchRequestDTO;
import com.accenture.ms.franchise.service.infrastructure.entrypoints.dto.response.BranchResponseDTO;
import com.accenture.ms.franchise.service.infrastructure.entrypoints.handler.IBranchHandler;
import com.accenture.ms.franchise.service.infrastructure.entrypoints.mapper.BranchRequestMapper;
import com.accenture.ms.franchise.service.infrastructure.entrypoints.mapper.BranchResponseMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class BranchHandler implements IBranchHandler {

    private final IFranchiseServicePort franchiseServicePort;
    private final BranchRequestMapper branchRequestMapper;
    private final BranchResponseMapper branchResponseMapper;

    @Override
    public Mono<BranchResponseDTO> saveBranch(BranchRequestDTO branchRequestDTO) {
        return Mono.just(branchRequestDTO)
                .map(branchRequestMapper::toModel)
                .flatMap(franchiseServicePort::saveBranch)
                .map(branchResponseMapper::toResponse)
                .onErrorMap(ex -> {
                    log.error("Error al guardar sucursal", ex);
                    if (ex instanceof BusinessException) {
                        return ex;
                    }
                    return new TechnicalException(ex, TechnicalMessage.INTERNAL_ERROR);
                });
    }
}
