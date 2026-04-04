package com.accenture.ms.franchise.service.infrastructure.entrypoints.mapper;

import com.accenture.ms.franchise.service.domain.model.ProductModel;
import com.accenture.ms.franchise.service.infrastructure.entrypoints.dto.request.ProductRequestDTO;
import org.springframework.stereotype.Component;

@Component
public class ProductRequestMapper {

    public ProductModel toModel(ProductRequestDTO dto) {
        return ProductModel.builder()
                .productName(dto.productName())
                .branchId(dto.branchId())
                .stock(dto.stock())
                .build();
    }
}
