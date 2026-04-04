package com.accenture.ms.franchise.service.dominio.model;

import com.accenture.ms.franchise.service.domain.enums.TechnicalMessage;
import com.accenture.ms.franchise.service.domain.exceptions.BusinessException;
import com.accenture.ms.franchise.service.domain.model.BranchModel;
import com.accenture.ms.franchise.service.domain.model.FranchiseModel;
import com.accenture.ms.franchise.service.domain.model.ProductModel;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ModelValidationTest {

    @Test
    void branchValidateBusinessRules_withoutId_generatesUuid() {
        BranchModel branch = BranchModel.builder()
                .branchName("Sucursal Norte")
                .franchiseId("F1")
                .build();

        StepVerifier.create(branch.validateBusinessRules())
                .assertNext(valid -> {
                    assertNotNull(valid.getBranchId());
                    assertNotNull(valid.getBranchName());
                })
                .verifyComplete();
    }

    @Test
    void productValidateBusinessRules_whenNameMissing_emitsBusinessException() {
        ProductModel product = ProductModel.builder()
                .branchId("B1")
                .stock(5)
                .build();

        StepVerifier.create(product.validateBusinessRules())
                .expectErrorSatisfies(ex -> {
                    BusinessException businessException = (BusinessException) ex;
                    org.junit.jupiter.api.Assertions.assertEquals(
                            TechnicalMessage.PRODUCT_NAME_REQUIRED,
                            businessException.getTechnicalMessage()
                    );
                })
                .verify();
    }

    @Test
    void franchiseValidateBusinessRules_whenNameMissing_emitsBusinessException() {
        FranchiseModel franchise = FranchiseModel.builder().build();

        StepVerifier.create(franchise.validateBusinessRules())
                .expectErrorSatisfies(ex -> {
                    BusinessException businessException = (BusinessException) ex;
                    org.junit.jupiter.api.Assertions.assertEquals(
                            TechnicalMessage.FRANCHISE_NAME_REQUIRED,
                            businessException.getTechnicalMessage()
                    );
                })
                .verify();
    }
}
