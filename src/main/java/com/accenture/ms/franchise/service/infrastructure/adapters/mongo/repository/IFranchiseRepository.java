package com.accenture.ms.franchise.service.infrastructure.adapters.mongo.repository;

import com.accenture.ms.franchise.service.infrastructure.adapters.mongo.document.FranchiseDocument;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface IFranchiseRepository extends ReactiveMongoRepository<FranchiseDocument, String> {
    Mono<Boolean> existsByFranchiseNameIgnoreCase(String name);
    Mono<FranchiseDocument> findByFranchiseId (String id);

    @Query("{ 'branches.branchId': ?0 }")
    Mono<FranchiseDocument> findByBranchId(String branchId);
}
