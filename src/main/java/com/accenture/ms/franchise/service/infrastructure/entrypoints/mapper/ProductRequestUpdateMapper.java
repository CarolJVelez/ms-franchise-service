package com.accenture.ms.franchise.service.infrastructure.entrypoints.mapper;

import com.accenture.ms.franchise.service.domain.model.ProductModel;
import com.accenture.ms.franchise.service.infrastructure.entrypoints.dto.request.ProductRequestUpdateDTO;
import org.springframework.stereotype.Component;

@Component
public class ProductRequestUpdateMapper {

    public ProductModel toModel(ProductRequestUpdateDTO dto) {
        return ProductModel.builder()
                .productId(dto.productId())
                .branchId(dto.branchId())
                .stock(dto.stock())
                .build();
    }
}
