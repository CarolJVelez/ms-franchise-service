package com.accenture.ms.franchise.service.domain.usecase;

import com.accenture.ms.franchise.service.domain.api.IFranchiseServicePort;
import com.accenture.ms.franchise.service.domain.exceptions.BusinessException;
import com.accenture.ms.franchise.service.domain.model.BranchModel;
import com.accenture.ms.franchise.service.domain.model.FranchiseModel;
import com.accenture.ms.franchise.service.domain.model.ProductBranchModel;
import com.accenture.ms.franchise.service.domain.model.ProductModel;
import com.accenture.ms.franchise.service.domain.spi.IFranchisePersistencePort;
import com.accenture.ms.franchise.service.domain.validation.FranchiseValidator;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
public class FranchiseUseCase implements IFranchiseServicePort {

    private final IFranchisePersistencePort franchisePersistencePort;
    private final FranchiseValidator validator;

    public FranchiseUseCase(IFranchisePersistencePort franchisePersistencePort,
                            FranchiseValidator validator) {
        this.franchisePersistencePort = franchisePersistencePort;
        this.validator = validator;
    }

    @Override
    public Mono<FranchiseModel> saveFranchise(FranchiseModel franchiseModel) {
        return franchiseModel.validateBusinessRules()
                .flatMap(model ->
                        validator.validateFranchiseNameNotExists(model.getFranchiseName())
                                .then(franchisePersistencePort.saveFranchise(model))
                )
                .doOnSuccess(saved ->
                        log.info("Franquicia '{}' creada con ID {}",
                                saved.getFranchiseName(), saved.getFranchiseId())
                )
                .doOnError(BusinessException.class, ex ->
                        log.warn("No se pudo crear la franquicia '{}': {}",
                                franchiseModel.getFranchiseName(), ex.getMessage())
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

                    List<BranchModel> branches = getBranches(franchise);

                    validator.validateBranchNotExists(branches, branchModel);

                    branches.add(branchModel);
                    franchise.setBranchModels(branches);

                    return franchisePersistencePort.saveFranchise(franchise)
                            .thenReturn(branchModel);

                })
                .doOnSuccess(saved ->
                        log.info("Sucursal '{}' creada con ID {}",
                                saved.getBranchName(), saved.getBranchId())
                )
                .doOnError(BusinessException.class, ex ->
                        log.warn("No se pudo crear la sucursal '{}': {}",
                                branchModel.getBranchName(), ex.getMessage())
                );
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
                })
                .doOnSuccess(saved ->
                        log.info("Producto '{}' creado con ID {}",
                                saved.getProductName(), saved.getProductId())
                )
                .doOnError(BusinessException.class, ex ->
                        log.warn("No se pudo crear el producto '{}': {}",
                                productModel.getProductName(), ex.getMessage())
                );
    }

    //Actualizar producto
    @Override
    public Mono<ProductModel> updateProductStock(ProductModel productModel) {
        return validator.validateAndGetFranchiseByBranch(productModel.getBranchId())
                .flatMap(franchise -> {

                    BranchModel branch = validator.getBranchOrThrow(
                            franchise, productModel.getBranchId());

                    List<ProductModel> products = getProducts(branch);

                    ProductModel existingProduct = validator.getProductOrThrow(
                            products, productModel.getProductId());

                    existingProduct.setStock(productModel.getStock());
                    existingProduct.setBranchId(productModel.getBranchId());

                    branch.setProductModels(products);

                    return franchisePersistencePort.saveFranchise(franchise)
                            .thenReturn(existingProduct);
                })
                .doOnSuccess(saved ->
                        log.info("Producto '{}' con ID {} actualizado",
                                saved.getProductName(), saved.getProductId())
                )
                .doOnError(BusinessException.class, ex ->
                        log.warn("No se pudo actualizar el producto '{}': {}",
                                productModel.getProductName(), ex.getMessage())
                );
    }

    //Obtener productos por mayor stock
    @Override
    public Mono<List<ProductBranchModel>> getTopStockProducts(String franchiseId) {
        return validator.validateAndGetFranchise(franchiseId)
                .map(franchise -> {
                    List<ProductBranchModel> productBranchModels = new ArrayList<>();

                    if (franchise.getBranchModels() != null) {
                        for (BranchModel branch : franchise.getBranchModels()) {
                            if (branch.getProductModels() != null && !branch.getProductModels().isEmpty()) {
                                ProductModel topProduct = branch.getProductModels().stream()
                                        .max(Comparator.comparing(ProductModel::getStock))
                                        .get();

                                productBranchModels.add(new ProductBranchModel(
                                        topProduct.getProductId(),
                                        topProduct.getProductName(),
                                        topProduct.getStock(),
                                        branch.getBranchId(),
                                        branch.getBranchName(),
                                        franchise.getFranchiseId(),
                                        franchise.getFranchiseName()
                                ));
                            }
                        }
                    }

                    log.info("Lista de productos de la franquicia '{}' generada con {} elementos",
                            franchiseId, productBranchModels.size());

                    return productBranchModels;
                })
                .doOnError(BusinessException.class, ex ->
                        log.warn("No se pudo obtener los productos de la franquicia '{}': {}",
                                franchiseId, ex.getMessage())
                );
    }

    //Eliminar producto
    @Override
    public Mono<Void> deleteProduct(String productId, String branchId) {
        return validator.validateAndGetFranchiseByBranch(branchId)
                .flatMap(franchise -> {
                    BranchModel branch = validator.getBranchOrThrow(
                            franchise, branchId);
                    List<ProductModel> products = getProducts(branch);

                    ProductModel existingProduct = validator.getProductOrThrow(
                            products, productId);

                    products.remove(existingProduct);

                    return franchisePersistencePort.saveFranchise(franchise)
                            .then();
                })
                .doOnSuccess(unused ->
                        log.info("Producto con ID '{}' eliminado de la sucursal '{}'",
                                productId, branchId)
                )
                .doOnError(BusinessException.class, ex ->
                        log.warn("No se pudo eliminar el producto '{}' de la sucursal '{}': {}",
                                productId, branchId, ex.getMessage())
                );
    }

    //Actualizar franquicia
    @Override
    public Mono<FranchiseModel> updateFranchise(FranchiseModel franchiseModel) {
        return validator.validateFranchiseNameNotExists(franchiseModel.getFranchiseName())
                .then(validator.validateAndGetFranchise(franchiseModel.getFranchiseId()))
                .flatMap(existing -> {
                    existing.setFranchiseName(franchiseModel.getFranchiseName());
                    return franchisePersistencePort.saveFranchise(existing);
                })
                .doOnSuccess(saved ->
                        log.info("Franquicia '{}' con ID {} actualizada",
                                saved.getFranchiseName(), saved.getFranchiseId())
                )
                .doOnError(BusinessException.class, ex ->
                        log.warn("No se pudo actualizar la franquicia '{}': {}",
                                franchiseModel.getFranchiseName(), ex.getMessage())
                );
    }

    //Actualizar sucursal
    @Override
    public Mono<BranchModel> updateBranch(BranchModel branchModel) {
        return validator.validateAndGetFranchiseByBranch(branchModel.getBranchId())
                .flatMap(franchise -> {

                    BranchModel branch = validator.getBranchOrThrow(franchise, branchModel.getBranchId());

                    List<BranchModel> branches = getBranches(franchise);

                    validator.validateBranchNotExists(branches, branchModel);

                    branch.setBranchName(branchModel.getBranchName());
                    branch.setFranchiseId(franchise.getFranchiseId());

                    return franchisePersistencePort.saveFranchise(franchise)
                            .thenReturn(branch);
                })
                .doOnSuccess(saved ->
                        log.info("Sucursal '{}' con ID {} actualizada",
                                saved.getBranchName(), saved.getBranchId())
                )
                .doOnError(BusinessException.class, ex ->
                        log.warn("No se pudo actualizar la sucursal '{}': {}",
                                branchModel.getBranchName(), ex.getMessage())
                );
    }

    @Override
    public Mono<ProductModel> updateProductName(ProductModel productModel) {
        return validator.validateAndGetFranchiseByBranch(productModel.getBranchId())
                .flatMap(franchise -> {

                    BranchModel branch = validator.getBranchOrThrow(
                            franchise, productModel.getBranchId());

                    List<ProductModel> products = getProducts(branch);

                    ProductModel existingProduct = validator.getProductOrThrow(
                            products, productModel.getProductId());

                    existingProduct.setProductName(productModel.getProductName());
                    existingProduct.setBranchId(productModel.getBranchId());

                    branch.setProductModels(products);

                    return franchisePersistencePort.saveFranchise(franchise)
                            .thenReturn(existingProduct);
                })
                .doOnSuccess(saved ->
                        log.info("El nombre del Producto '{}' con ID {} actualizado",
                                saved.getProductName(), saved.getProductId())
                )
                .doOnError(BusinessException.class, ex ->
                        log.warn("No se pudo actualizar el nombre del producto '{}': {}",
                                productModel.getProductName(), ex.getMessage())
                );
    }

    //Obtener productos
    private List<ProductModel> getProducts(BranchModel branch) {
        if (branch.getProductModels() == null) {
            branch.setProductModels(new ArrayList<>());
        }
        return branch.getProductModels();
    }

    //Obtener sucursales
    private List<BranchModel> getBranches(FranchiseModel franchiseModel) {
        if (franchiseModel.getBranchModels() == null) {
            franchiseModel.setBranchModels(new ArrayList<>());
        }
        return franchiseModel.getBranchModels();
    }
}