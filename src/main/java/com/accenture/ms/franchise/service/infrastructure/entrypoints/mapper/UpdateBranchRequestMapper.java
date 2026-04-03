package com.accenture.ms.franchise.service.infrastructure.entrypoints.mapper;

import com.accenture.ms.franchise.service.domain.model.BranchModel;
import com.accenture.ms.franchise.service.infrastructure.entrypoints.dto.request.BranchUpdateRequestDTO;
import org.springframework.stereotype.Component;

@Component
public class UpdateBranchRequestMapper {

    public BranchModel toModel(BranchUpdateRequestDTO dto) {
        return BranchModel.builder()
                .branchId(dto.branchId())
                .branchName(dto.branchName())
                .build();
    }
}
