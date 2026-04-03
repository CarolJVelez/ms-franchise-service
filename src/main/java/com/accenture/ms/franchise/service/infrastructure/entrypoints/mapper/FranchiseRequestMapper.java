package com.accenture.ms.franchise.service.infrastructure.entrypoints.mapper;

import com.accenture.ms.franchise.service.domain.model.FranchiseModel;
import com.accenture.ms.franchise.service.infrastructure.entrypoints.dto.request.FranchiseRequestDTO;
import org.springframework.stereotype.Component;

@Component
public class FranchiseRequestMapper {

    public FranchiseModel toModel(FranchiseRequestDTO dto) {
        return FranchiseModel.builder()
                .franchiseName(dto.franchiseName())
                .build();
    }
}
