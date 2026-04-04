package com.accenture.ms.franchise.service.domain.spi;

import com.accenture.ms.franchise.service.domain.model.FranchiseModel;
import reactor.core.publisher.Mono;

public interface IFranchisePersistencePort {
    Mono<Boolean> existsByName(String name);
    Mono<FranchiseModel> saveFranchise(FranchiseModel franchiseModel);
    Mono<FranchiseModel> findByFranchiseId(String id);
    Mono<FranchiseModel> findByBranchId(String id);
}
