package com.accenture.ms.franchise.service.infrastructure.entrypoints.mapper;

import com.accenture.ms.franchise.service.domain.model.FranchiseModel;
import com.accenture.ms.franchise.service.infrastructure.entrypoints.dto.response.FranchiseResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class FranchiseResponseMapper {

    public FranchiseResponseDTO toResponse(FranchiseModel franchiseModel) {
        return new FranchiseResponseDTO(
                franchiseModel.getFranchiseId(),
                franchiseModel.getFranchiseName()
        );
    }
}
