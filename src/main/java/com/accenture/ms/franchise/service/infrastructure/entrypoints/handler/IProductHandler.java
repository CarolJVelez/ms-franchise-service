package com.accenture.ms.franchise.service.infrastructure.entrypoints.handler;

import com.accenture.ms.franchise.service.infrastructure.entrypoints.dto.request.ProductRequestDTO;
import com.accenture.ms.franchise.service.infrastructure.entrypoints.dto.request.ProductRequestUpdateDTO;
import com.accenture.ms.franchise.service.infrastructure.entrypoints.dto.response.ProductResponseDTO;
import reactor.core.publisher.Mono;

public interface IProductHandler {
    Mono<ProductResponseDTO> saveProduct(ProductRequestDTO productRequestDTO);
    Mono<ProductResponseDTO> updateStockProduct(ProductRequestUpdateDTO productRequestUpdateDTO);
}
