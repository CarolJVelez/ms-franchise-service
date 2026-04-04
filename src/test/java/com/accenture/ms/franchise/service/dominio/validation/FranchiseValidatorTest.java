package com.accenture.ms.franchise.service.dominio.validation;

import com.accenture.ms.franchise.service.domain.enums.TechnicalMessage;
import com.accenture.ms.franchise.service.domain.exceptions.BusinessException;
import com.accenture.ms.franchise.service.domain.model.BranchModel;
import com.accenture.ms.franchise.service.domain.model.FranchiseModel;
import com.accenture.ms.franchise.service.domain.model.ProductModel;
import com.accenture.ms.franchise.service.domain.spi.IFranchisePersistencePort;
import com.accenture.ms.franchise.service.domain.validation.FranchiseValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class FranchiseValidatorTest {

    private IFranchisePersistencePort persistencePort;
    private FranchiseValidator validator;

    @BeforeEach
    void setUp() {
        persistencePort = mock(IFranchisePersistencePort.class);
        validator = new FranchiseValidator(persistencePort);
    }

    @Test
    void validateAndGetFranchise_notFound_emitsBusinessException() {
        when(persistencePort.findByFranchiseId("F1")).thenReturn(Mono.empty());

        StepVerifier.create(validator.validateAndGetFranchise("F1"))
                .expectErrorSatisfies(ex -> {
                    BusinessException businessException = (BusinessException) ex;
                    assertEquals(TechnicalMessage.FRANCHISE_NOT_FOUND, businessException.getTechnicalMessage());
                    assertEquals("La Franquicia no existe.", businessException.getMessage());
                })
                .verify();
    }

    @Test
    void validateFranchiseNameNotExists_whenNameExists_emitsBusinessException() {
        when(persistencePort.existsByName("Franquicia Centro")).thenReturn(Mono.just(true));

        StepVerifier.create(validator.validateFranchiseNameNotExists("Franquicia Centro"))
                .expectErrorSatisfies(ex -> {
                    BusinessException businessException = (BusinessException) ex;
                    assertEquals(TechnicalMessage.FRANCHISE_NAME_ALREADY_EXISTS, businessException.getTechnicalMessage());
                    assertEquals("El nombre de la franquicia ya existe.", businessException.getMessage());
                })
                .verify();
    }

    @Test
    void getBranchOrThrow_whenBranchDoesNotExist_throwsBusinessException() {
        FranchiseModel franchise = FranchiseModel.builder()
                .franchiseId("F1")
                .branchModels(new ArrayList<>())
                .build();

        BusinessException exception = assertThrows(BusinessException.class,
                () -> validator.getBranchOrThrow(franchise, "B1"));

        assertEquals(TechnicalMessage.BRANCH_NOT_FOUND, exception.getTechnicalMessage());
        assertEquals("La Sucursal no existe.", exception.getMessage());
    }

    @Test
    void getProductOrThrow_whenProductDoesNotExist_throwsBusinessException() {
        List<ProductModel> products = new ArrayList<>();

        BusinessException exception = assertThrows(BusinessException.class,
                () -> validator.getProductOrThrow(products, "P1"));

        assertEquals(TechnicalMessage.PRODUCT_NOT_FOUND, exception.getTechnicalMessage());
        assertEquals("El producto no existe.", exception.getMessage());
    }

    @Test
    void validateBranchNotExists_whenBranchExists_throwsBusinessException() {
        BranchModel existing = BranchModel.builder().branchName("Sucursal Norte").build();
        BranchModel request = BranchModel.builder().branchName("Sucursal Norte").build();

        BusinessException exception = assertThrows(BusinessException.class,
                () -> validator.validateBranchNotExists(List.of(existing), request));

        assertEquals(TechnicalMessage.BRANCH_NAME_ALREADY_EXISTS, exception.getTechnicalMessage());
    }
}
