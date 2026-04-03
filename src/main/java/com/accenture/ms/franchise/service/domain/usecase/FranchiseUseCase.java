package com.accenture.ms.franchise.service.domain.usecase;

import com.accenture.ms.franchise.service.domain.api.IFranchiseServicePort;
import com.accenture.ms.franchise.service.domain.model.BranchModel;
import com.accenture.ms.franchise.service.domain.model.FranchiseModel;
import com.accenture.ms.franchise.service.domain.model.ProductModel;
import com.accenture.ms.franchise.service.domain.spi.IFranchisePersistencePort;
import com.accenture.ms.franchise.service.domain.validation.FranchiseValidator;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

public class FranchiseUseCase implements IFranchiseServicePort {

    private final IFranchisePersistencePort franchisePersistencePort;
    private final FranchiseValidator validator;

    public FranchiseUseCase(IFranchisePersistencePort franchisePersistencePort,
                            FranchiseValidator validator) {
        this.franchisePersistencePort = franchisePersistencePort;
        this.validator = validator;
    }

    // Guardar franchise
    @Override
    public Mono<FranchiseModel> saveFranchise(FranchiseModel franchiseModel) {
        return franchiseModel.validateBusinessRules()
                .flatMap(model ->
                        validator.validateFranchiseNameNotExists(model.getFranchiseName())
                                .then(franchisePersistencePort.saveFranchise(model))
                );
    }

    // Guardar branch
    @Override
    public Mono<BranchModel> saveBranch(BranchModel branchModel) {
        return branchModel.validateBusinessRules()
                .flatMap(branch ->
                        validator.validateAndGetFranchise(branch.getFranchiseId())
                )
                .flatMap(franchise -> {

                    List<BranchModel> branches = franchise.getBranchModels() == null
                            ? new ArrayList<>()
                            : new ArrayList<>(franchise.getBranchModels());

                    validator.validateBranchNotExists(branches, branchModel);

                    branches.add(branchModel);
                    franchise.setBranchModels(branches);

                    return franchisePersistencePort.saveFranchise(franchise)
                            .thenReturn(branchModel);
                });
    }

    // Guardar product
    @Override
    public Mono<ProductModel> saveProduct(ProductModel productModel) {
        return productModel.validateBusinessRules()
                .flatMap(product ->
                        validator.validateAndGetFranchiseByBranch(product.getBranchId())
                )
                .flatMap(franchise -> {

                    BranchModel branch = validator.getBranchOrThrow(
                            franchise, productModel.getBranchId());

                    List<ProductModel> products = getProducts(branch);

                    validator.validateProductNotExists(products, productModel);

                    products.add(productModel);
                    branch.setProductModels(products);

                    return franchisePersistencePort.saveFranchise(franchise)
                            .thenReturn(productModel);
                });
    }

    //Actualizar producto
    @Override
    public Mono<ProductModel> updateProduct(ProductModel productModel) {
        return productModel.validateBusinessRules()
                .flatMap(product ->
                        validator.validateAndGetFranchiseByBranch(product.getBranchId())
                )
                .flatMap(franchise -> {

                    BranchModel branch = validator.getBranchOrThrow(
                            franchise, productModel.getBranchId());

                    List<ProductModel> products = getProducts(branch);

                    ProductModel existingProduct = validator.getProductOrThrow(
                            products, productModel.getProductId());

                    existingProduct.setStock(productModel.getStock());

                    branch.setProductModels(products);

                    return franchisePersistencePort.saveFranchise(franchise)
                            .thenReturn(existingProduct);
                });
    }

    private List<ProductModel> getProducts(BranchModel branch) {
        return branch.getProductModels() == null
                ? new ArrayList<>()
                : new ArrayList<>(branch.getProductModels());
    }
}