package com.accenture.ms.franchise.service.infrastructure.entrypoints.mapper;

import com.accenture.ms.franchise.service.domain.model.ProductModel;
import com.accenture.ms.franchise.service.infrastructure.entrypoints.dto.request.ProductRequestUpdateDTO;
import com.accenture.ms.franchise.service.infrastructure.entrypoints.dto.request.ProductRequestUpdateNameDTO;
import org.springframework.stereotype.Component;

@Component
public class UpdateProductRequestMapper {

    public ProductModel toModel(ProductRequestUpdateDTO dto) {
        return ProductModel.builder()
                .productId(dto.productId())
                .branchId(dto.branchId())
                .stock(dto.stock())
                .build();
    }

    public ProductModel toModelName(ProductRequestUpdateNameDTO dto) {
        return ProductModel.builder()
                .productId(dto.productId())
                .productName(dto.productName())
                .branchId(dto.branchId())
                .build();
    }
}
