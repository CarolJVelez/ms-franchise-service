package com.accenture.ms.franchise.service.domain.model;

import com.accenture.ms.franchise.service.domain.enums.TechnicalMessage;
import com.accenture.ms.franchise.service.domain.exceptions.BusinessException;
import lombok.*;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FranchiseModel {
    private String franchiseId;
    private String franchiseName;
    @Builder.Default
    private List<BranchModel> branchModels = new ArrayList<>();

    public Mono<FranchiseModel> validateBusinessRules() {
        if (this.franchiseName == null || this.franchiseName.isBlank()) {
            return Mono.error(new BusinessException(TechnicalMessage.FRANCHISE_NAME_REQUIRED));
        }
        return Mono.just(this);
    }
}
