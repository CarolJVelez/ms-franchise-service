package com.accenture.ms.franchise.service.infrastructure.adapters.mongo.adapter;

import com.accenture.ms.franchise.service.domain.model.FranchiseModel;
import com.accenture.ms.franchise.service.domain.spi.IFranchisePersistencePort;
import com.accenture.ms.franchise.service.infrastructure.adapters.mongo.mapper.FranchiseDocumentMapper;
import com.accenture.ms.franchise.service.infrastructure.adapters.mongo.repository.IFranchiseRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class FranchiseMongoAdapter implements IFranchisePersistencePort {

    private final IFranchiseRepository franchiseRepository;
    private final FranchiseDocumentMapper franchiseDocumentMapper;

    @Override
    public Mono<Boolean> existsByName(String name) {
        return franchiseRepository.existsByFranchiseNameIgnoreCase(name.trim());
    }

    @Override
    public Mono<FranchiseModel> saveFranchise(FranchiseModel franchiseModel) {
        return franchiseRepository.save(franchiseDocumentMapper.toDocument(franchiseModel))
                .map(franchiseDocumentMapper::toModel);
    }

    @Override
    public Mono<FranchiseModel> findByFranchiseId(String id) {
        return franchiseRepository.findByFranchiseId(id.trim())
                .map(franchiseDocumentMapper::toModel);
    }

    @Override
    public Mono<FranchiseModel> findByBranchId(String branchId) {
        return franchiseRepository.findByBranchId(branchId)
                .map(franchiseDocumentMapper::toModel);
    }
}
