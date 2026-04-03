package com.accenture.ms.franchise.service.infrastructure.entrypoints.mapper;

import com.accenture.ms.franchise.service.domain.model.BranchModel;
import com.accenture.ms.franchise.service.infrastructure.entrypoints.dto.request.BranchRequestDTO;
import org.springframework.stereotype.Component;

@Component
public class BranchRequestMapper {

    public BranchModel toModel(BranchRequestDTO dto) {
        return BranchModel.builder()
                .branchName(dto.branchName())
                .franchiseId(dto.franchiseId())
                .build();
    }
}
