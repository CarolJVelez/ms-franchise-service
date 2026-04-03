package com.accenture.ms.franchise.service.domain.api;

import com.accenture.ms.franchise.service.domain.model.BranchModel;
import com.accenture.ms.franchise.service.domain.model.FranchiseModel;
import com.accenture.ms.franchise.service.domain.model.ProductModel;
import reactor.core.publisher.Mono;

public interface IFranchiseServicePort {
    Mono<FranchiseModel> saveFranchise(FranchiseModel franchiseModel);
    Mono<BranchModel> saveBranch(BranchModel branchModel);
    Mono<ProductModel> saveProduct(ProductModel productModel);
    Mono<ProductModel> updateProduct(ProductModel productModel);
}
