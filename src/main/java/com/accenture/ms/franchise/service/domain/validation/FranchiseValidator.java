package com.accenture.ms.franchise.service.domain.validation;

import com.accenture.ms.franchise.service.domain.enums.TechnicalMessage;
import com.accenture.ms.franchise.service.domain.exceptions.BusinessException;
import com.accenture.ms.franchise.service.domain.model.BranchModel;
import com.accenture.ms.franchise.service.domain.model.FranchiseModel;
import com.accenture.ms.franchise.service.domain.model.ProductModel;
import com.accenture.ms.franchise.service.domain.spi.IFranchisePersistencePort;
import reactor.core.publisher.Mono;

import java.util.List;

public class FranchiseValidator {

    private final IFranchisePersistencePort franchisePersistencePort;

    public FranchiseValidator(IFranchisePersistencePort franchisePersistencePort) {
        this.franchisePersistencePort = franchisePersistencePort;
    }

    // Validar y obtener franquicia por ID
    public Mono<FranchiseModel> validateAndGetFranchise(String franchiseId) {
        return franchisePersistencePort.findByFranchiseId(franchiseId)
                .flatMap(Mono::just)
                .switchIfEmpty(Mono.error(
                        new BusinessException(TechnicalMessage.FRANCHISE_NOT_FOUND)
                ));
    }

    //Validar y obtener franquicia por branchId
    public Mono<FranchiseModel> validateAndGetFranchiseByBranch(String branchId) {
        return franchisePersistencePort.findByBranchId(branchId)
                .flatMap(Mono::just)
                .switchIfEmpty(Mono.error(
                        new BusinessException(TechnicalMessage.BRANCH_NOT_FOUND)
                ));
    }

    // Validar que franquicia NO exista
    public Mono<Void> validateFranchiseNameNotExists(String name) {
        return franchisePersistencePort.existsByName(name)
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new BusinessException(
                                TechnicalMessage.FRANCHISE_NAME_ALREADY_EXISTS));
                    }
                    return Mono.empty();
                });
    }

    // Validar que branch no exista
    public void validateBranchNotExists(List<BranchModel> branches, BranchModel branch) {
        boolean exists = branches.stream()
                .anyMatch(b -> b.getBranchName()
                        .equalsIgnoreCase(branch.getBranchName()));

        if (exists) {
            throw new BusinessException(TechnicalMessage.BRANCH_NAME_ALREADY_EXISTS);
        }
    }

    // Validar que producto no exista
    public void validateProductNotExists(List<ProductModel> products, ProductModel product) {
        boolean exists = products.stream()
                .anyMatch(p -> p.getProductName()
                        .equalsIgnoreCase(product.getProductName()));

        if (exists) {
            throw new BusinessException(TechnicalMessage.PRODUCT_NAME_ALREADY_EXISTS);
        }
    }
// obtener branch con la franquicia
    public BranchModel getBranchOrThrow(FranchiseModel franchise, String branchId) {
        return franchise.getBranchModels().stream()
                .filter(b -> b.getBranchId().equals(branchId))
                .findFirst()
                .orElseThrow(() ->
                        new BusinessException(TechnicalMessage.BRANCH_NOT_FOUND));
    }

    public ProductModel getProductOrThrow(List<ProductModel> products, String productId) {
        return products.stream()
                .filter(p -> p.getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() ->
                        new BusinessException(TechnicalMessage.PRODUCT_NOT_FOUND));
    }
}