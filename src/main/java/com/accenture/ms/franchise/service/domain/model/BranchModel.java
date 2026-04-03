package com.accenture.ms.franchise.service.domain.model;

import com.accenture.ms.franchise.service.domain.enums.TechnicalMessage;
import com.accenture.ms.franchise.service.domain.exceptions.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class BranchModel {
    private String branchId;
    private String branchName;
    private String franchiseId;
    @Builder.Default
    private List<ProductModel> productModels = new ArrayList<>();

    public Mono<BranchModel> validateBusinessRules() {
        if (this.branchName == null || this.branchName.isBlank()) {
            return Mono.error(new BusinessException(TechnicalMessage.BRANCH_NAME_REQUIRED));
        }

        if (this.franchiseId == null || this.franchiseId.isBlank()) {
            return Mono.error(new BusinessException(TechnicalMessage.FRANCHISE_ID_REQUIRED));
        }

        if (branchId == null || branchId.isBlank()) {
            branchId = UUID.randomUUID().toString();
        }
        return Mono.just(this);
    }
}
