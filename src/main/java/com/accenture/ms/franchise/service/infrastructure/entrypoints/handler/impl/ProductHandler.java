package com.accenture.ms.franchise.service.infrastructure.entrypoints.handler.impl;

import com.accenture.ms.franchise.service.domain.api.IFranchiseServicePort;
import com.accenture.ms.franchise.service.domain.enums.TechnicalMessage;
import com.accenture.ms.franchise.service.domain.exceptions.BusinessException;
import com.accenture.ms.franchise.service.domain.exceptions.TechnicalException;
import com.accenture.ms.franchise.service.infrastructure.entrypoints.dto.request.ProductRequestDTO;
import com.accenture.ms.franchise.service.infrastructure.entrypoints.dto.request.ProductRequestUpdateDTO;
import com.accenture.ms.franchise.service.infrastructure.entrypoints.dto.request.ProductRequestUpdateNameDTO;
import com.accenture.ms.franchise.service.infrastructure.entrypoints.dto.response.ProductResponseDTO;
import com.accenture.ms.franchise.service.infrastructure.entrypoints.handler.IProductHandler;
import com.accenture.ms.franchise.service.infrastructure.entrypoints.mapper.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductHandler implements IProductHandler {

    private final IFranchiseServicePort franchiseServicePort;
    private final ProductRequestMapper productRequestMapper;
    private final ProductResponseMapper productResponseMapper;
    private final UpdateProductRequestMapper updateProductRequestMapper;

    @Override
    public Mono<ProductResponseDTO> saveProduct(ProductRequestDTO productRequestDTO) {
        return Mono.just(productRequestDTO)
                .map(productRequestMapper::toModel)
                .flatMap(franchiseServicePort::saveProduct)
                .map(productResponseMapper::toResponse)
                .onErrorMap(ex -> {
                    log.error("Error al guardar el producto", ex);
                    if (ex instanceof BusinessException) {
                        return ex;
                    }
                    return new TechnicalException(ex, TechnicalMessage.INTERNAL_ERROR);
                });
    }

    @Override
    public Mono<ProductResponseDTO> updateStockProduct(ProductRequestUpdateDTO productRequestUpdateDTO) {
        return Mono.just(productRequestUpdateDTO)
                .map(updateProductRequestMapper::toModel)
                .flatMap(franchiseServicePort::updateProductStock)
                .map(productResponseMapper::toResponse)
                .onErrorMap(ex -> {
                    log.error("Error al actualizar el producto", ex);
                    if(ex instanceof BusinessException){
                        return ex;
                    }
                    return new TechnicalException(ex, TechnicalMessage.INTERNAL_ERROR);
                });
    }

    @Override
    public Mono<Void> deleteProduct(String productId, String branchId) {
        return franchiseServicePort.deleteProduct(productId, branchId)
                .onErrorMap(ex -> {
                    log.error("Error al eliminar el producto", ex);
                    if (ex instanceof BusinessException) return ex;
                    return new TechnicalException(ex, TechnicalMessage.INTERNAL_ERROR);
                });
    }

    @Override
    public Mono<ProductResponseDTO> updateProductName(ProductRequestUpdateNameDTO productRequestUpdateNameDTO) {
        return Mono.just(productRequestUpdateNameDTO)
                .map(updateProductRequestMapper::toModelName)
                .flatMap(franchiseServicePort::updateProductName)
                .map(productResponseMapper::toResponse)
                .onErrorMap(ex -> {
                    log.error("Error al actualizar el nombre del producto", ex);
                    if(ex instanceof BusinessException){
                        return ex;
                    }
                    return new TechnicalException(ex, TechnicalMessage.INTERNAL_ERROR);
                });
    }

}
