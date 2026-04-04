package com.accenture.ms.franchise.service.domain.api;

import com.accenture.ms.franchise.service.domain.model.BranchModel;
import com.accenture.ms.franchise.service.domain.model.FranchiseModel;
import com.accenture.ms.franchise.service.domain.model.ProductBranchModel;
import com.accenture.ms.franchise.service.domain.model.ProductModel;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IFranchiseServicePort {
    Mono<FranchiseModel> saveFranchise(FranchiseModel franchiseModel);
    Mono<BranchModel> saveBranch(BranchModel branchModel);
    Mono<ProductModel> saveProduct(ProductModel productModel);
    Mono<ProductModel> updateProductStock(ProductModel productModel);
    Mono<List<ProductBranchModel>> getTopStockProducts(String franchiseId);
    Mono<Void> deleteProduct(String productId, String branchId);
    Mono<FranchiseModel> updateFranchise(FranchiseModel franchiseModel);
    Mono<BranchModel> updateBranch(BranchModel branchModel);
    Mono<ProductModel> updateProductName(ProductModel productModel);
}
