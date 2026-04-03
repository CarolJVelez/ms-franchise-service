package com.accenture.ms.franchise.service.infrastructure.entrypoints.mapper;

import com.accenture.ms.franchise.service.domain.model.ProductModel;
import com.accenture.ms.franchise.service.infrastructure.entrypoints.dto.response.ProductResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class ProductResponseMapper {

    public ProductResponseDTO toResponse(ProductModel productModel) {
        return new ProductResponseDTO(
                productModel.getProductId(),
                productModel.getProductName(),
                productModel.getStock(),
                productModel.getBranchId()
        );
    }
}
