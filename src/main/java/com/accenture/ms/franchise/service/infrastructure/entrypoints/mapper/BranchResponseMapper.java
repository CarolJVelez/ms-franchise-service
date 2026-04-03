package com.accenture.ms.franchise.service.infrastructure.entrypoints.mapper;

import com.accenture.ms.franchise.service.domain.model.BranchModel;
import com.accenture.ms.franchise.service.infrastructure.entrypoints.dto.response.BranchResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class BranchResponseMapper {

    public BranchResponseDTO toResponse(BranchModel branchModel) {
        return new BranchResponseDTO(
                branchModel.getBranchId(),
                branchModel.getBranchName(),
                branchModel.getFranchiseId()
        );
    }
}
