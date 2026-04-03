package com.accenture.ms.franchise.service.domain.model;

import com.accenture.ms.franchise.service.domain.enums.TechnicalMessage;
import com.accenture.ms.franchise.service.domain.exceptions.BusinessException;
import lombok.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductModel {
    private String productId;
    private String productName;
    private Integer stock;
    private String branchId;

    public Mono<ProductModel> validateBusinessRules() {
        if (this.productName == null || this.productName.isBlank()) {
            return Mono.error(new BusinessException(TechnicalMessage.PRODUCT_NAME_REQUIRED));
        }

        if (this.branchId == null || this.branchId.isBlank()) {
            return Mono.error(new BusinessException(TechnicalMessage.BRANCH_ID_REQUIRED));
        }

        if (this.stock == null || this.stock < 0) {
            return Mono.error(new BusinessException(TechnicalMessage.STOCK_REQUIRED));
        }

        if (this.productId == null || this.productId.isBlank()) {
            this.productId = UUID.randomUUID().toString();
        }
        return Mono.just(this);
    }
}
