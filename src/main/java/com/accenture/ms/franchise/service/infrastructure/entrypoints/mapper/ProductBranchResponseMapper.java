package com.accenture.ms.franchise.service.infrastructure.entrypoints.mapper;

import com.accenture.ms.franchise.service.domain.model.ProductBranchModel;
import com.accenture.ms.franchise.service.infrastructure.entrypoints.dto.response.ProductBranchResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class ProductBranchResponseMapper {

    public ProductBranchResponseDTO toResponse(ProductBranchModel productBranchModel) {
        return new ProductBranchResponseDTO(
                productBranchModel.getProductId(),
                productBranchModel.getProductName(),
                productBranchModel.getStock(),
                productBranchModel.getBranchId(),
                productBranchModel.getBranchName(),
                productBranchModel.getFranchiseId(),
                productBranchModel.getFranchiseName()
        );
    }
}
