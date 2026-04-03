package com.accenture.ms.franchise.service.infrastructure.entrypoints.mapper;

import com.accenture.ms.franchise.service.domain.model.FranchiseModel;
import com.accenture.ms.franchise.service.infrastructure.entrypoints.dto.request.FranchiseUpdateRequestDTO;
import org.springframework.stereotype.Component;

@Component
public class UpdateFranchiseRequestMapper {

    public FranchiseModel toModel(FranchiseUpdateRequestDTO dto) {
        return FranchiseModel.builder()
                .franchiseId(dto.franchiseId())
                .franchiseName(dto.franchiseName())
                .build();
    }
}
