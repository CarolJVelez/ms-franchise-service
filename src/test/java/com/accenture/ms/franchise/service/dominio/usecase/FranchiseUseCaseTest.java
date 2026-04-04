package com.accenture.ms.franchise.service.dominio.usecase;

import com.accenture.ms.franchise.service.domain.enums.TechnicalMessage;
import com.accenture.ms.franchise.service.domain.exceptions.BusinessException;
import com.accenture.ms.franchise.service.domain.model.BranchModel;
import com.accenture.ms.franchise.service.domain.model.FranchiseModel;
import com.accenture.ms.franchise.service.domain.model.ProductModel;
import com.accenture.ms.franchise.service.domain.spi.IFranchisePersistencePort;
import com.accenture.ms.franchise.service.domain.usecase.FranchiseUseCase;
import com.accenture.ms.franchise.service.domain.validation.FranchiseValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FranchiseUseCaseTest {

    @Mock
    private IFranchisePersistencePort persistencePort;

    @Mock
    private FranchiseValidator validator;

    private FranchiseUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new FranchiseUseCase(persistencePort, validator);
    }

    @Test
    void saveFranchise_success() {
        FranchiseModel franchise = FranchiseModel.builder()
                .franchiseId("F1")
                .franchiseName("Franquicia Centro")
                .build();

        when(validator.validateFranchiseNameNotExists("Franquicia Centro")).thenReturn(Mono.empty());
        when(persistencePort.saveFranchise(franchise)).thenReturn(Mono.just(franchise));

        StepVerifier.create(useCase.saveFranchise(franchise))
                .assertNext(saved -> {
                    assertEquals("F1", saved.getFranchiseId());
                    assertEquals("Franquicia Centro", saved.getFranchiseName());
                })
                .verifyComplete();

        verify(validator).validateFranchiseNameNotExists("Franquicia Centro");
        verify(persistencePort).saveFranchise(franchise);
    }

    @Test
    void saveFranchise_nameExists_throwsBusinessException() {
        FranchiseModel franchise = FranchiseModel.builder()
                .franchiseId("F1")
                .franchiseName("Franquicia Centro")
                .build();

        when(validator.validateFranchiseNameNotExists("Franquicia Centro"))
                .thenReturn(Mono.error(new BusinessException(
                        TechnicalMessage.FRANCHISE_NAME_ALREADY_EXISTS)));

        StepVerifier.create(useCase.saveFranchise(franchise))
                .expectError(BusinessException.class)
                .verify();

        verify(validator).validateFranchiseNameNotExists("Franquicia Centro");
        verify(persistencePort, never()).saveFranchise(any());
    }

    @Test
    void saveBranch_success() {
        BranchModel branch = BranchModel.builder()
                .branchId("B1")
                .franchiseId("F1")
                .branchName("Sucursal Norte")
                .build();

        FranchiseModel franchise = FranchiseModel.builder()
                .franchiseId("F1")
                .franchiseName("Franquicia Centro")
                .branchModels(new ArrayList<>())
                .build();

        when(validator.validateAndGetFranchise("F1")).thenReturn(Mono.just(franchise));
        doNothing().when(validator).validateBranchNotExists(anyList(), eq(branch));
        when(persistencePort.saveFranchise(franchise)).thenReturn(Mono.just(franchise));

        StepVerifier.create(useCase.saveBranch(branch))
                .assertNext(saved -> {
                    assertEquals("B1", saved.getBranchId());
                    assertEquals("Sucursal Norte", saved.getBranchName());
                    assertEquals(1, franchise.getBranchModels().size());
                })
                .verifyComplete();

        verify(validator).validateAndGetFranchise("F1");
        verify(validator).validateBranchNotExists(anyList(), eq(branch));
        verify(persistencePort).saveFranchise(franchise);
    }

    @Test
    void saveProduct_success() {
        ProductModel product = ProductModel.builder()
                .productId("P1")
                .productName("Coca Cola")
                .stock(10)
                .branchId("B1")
                .build();

        BranchModel branch = BranchModel.builder()
                .branchId("B1")
                .branchName("Sucursal Norte")
                .productModels(new ArrayList<>())
                .build();

        FranchiseModel franchise = FranchiseModel.builder()
                .franchiseId("F1")
                .franchiseName("Franquicia Centro")
                .branchModels(new ArrayList<>(List.of(branch)))
                .build();

        when(validator.validateAndGetFranchiseByBranch("B1")).thenReturn(Mono.just(franchise));
        when(validator.getBranchOrThrow(franchise, "B1")).thenReturn(branch);
        doNothing().when(validator).validateProductNotExists(anyList(), eq(product));
        when(persistencePort.saveFranchise(franchise)).thenReturn(Mono.just(franchise));

        StepVerifier.create(useCase.saveProduct(product))
                .assertNext(saved -> {
                    assertEquals("P1", saved.getProductId());
                    assertEquals("Coca Cola", saved.getProductName());
                    assertEquals(1, branch.getProductModels().size());
                })
                .verifyComplete();

        verify(validator).validateAndGetFranchiseByBranch("B1");
        verify(validator).getBranchOrThrow(franchise, "B1");
        verify(validator).validateProductNotExists(anyList(), eq(product));
        verify(persistencePort).saveFranchise(franchise);
    }

    @Test
    void updateProductStock_success() {
        ProductModel request = ProductModel.builder()
                .productId("P1")
                .branchId("B1")
                .stock(25)
                .build();

        ProductModel existing = ProductModel.builder()
                .productId("P1")
                .productName("Coca Cola")
                .stock(10)
                .branchId("B1")
                .build();

        BranchModel branch = BranchModel.builder()
                .branchId("B1")
                .productModels(new ArrayList<>(List.of(existing)))
                .build();

        FranchiseModel franchise = FranchiseModel.builder()
                .franchiseId("F1")
                .branchModels(new ArrayList<>(List.of(branch)))
                .build();

        when(validator.validateAndGetFranchiseByBranch("B1")).thenReturn(Mono.just(franchise));
        when(validator.getBranchOrThrow(franchise, "B1")).thenReturn(branch);
        when(validator.getProductOrThrow(branch.getProductModels(), "P1")).thenReturn(existing);
        when(persistencePort.saveFranchise(franchise)).thenReturn(Mono.just(franchise));

        StepVerifier.create(useCase.updateProductStock(request))
                .assertNext(updated -> {
                    assertEquals("P1", updated.getProductId());
                    assertEquals(25, updated.getStock());
                })
                .verifyComplete();

        verify(persistencePort).saveFranchise(franchise);
    }

    @Test
    void updateProductName_success() {
        ProductModel request = ProductModel.builder()
                .productId("P1")
                .branchId("B1")
                .productName("Pepsi")
                .build();

        ProductModel existing = ProductModel.builder()
                .productId("P1")
                .productName("Coca Cola")
                .stock(10)
                .branchId("B1")
                .build();

        BranchModel branch = BranchModel.builder()
                .branchId("B1")
                .productModels(new ArrayList<>(List.of(existing)))
                .build();

        FranchiseModel franchise = FranchiseModel.builder()
                .franchiseId("F1")
                .branchModels(new ArrayList<>(List.of(branch)))
                .build();

        when(validator.validateAndGetFranchiseByBranch("B1")).thenReturn(Mono.just(franchise));
        when(validator.getBranchOrThrow(franchise, "B1")).thenReturn(branch);
        when(validator.getProductOrThrow(branch.getProductModels(), "P1")).thenReturn(existing);
        when(persistencePort.saveFranchise(franchise)).thenReturn(Mono.just(franchise));

        StepVerifier.create(useCase.updateProductName(request))
                .assertNext(updated -> {
                    assertEquals("P1", updated.getProductId());
                    assertEquals("Pepsi", updated.getProductName());
                })
                .verifyComplete();

        verify(persistencePort).saveFranchise(franchise);
    }

    @Test
    void updateBranch_success() {
        BranchModel request = BranchModel.builder()
                .branchId("B1")
                .branchName("Sucursal Sur")
                .build();

        BranchModel existingBranch = BranchModel.builder()
                .branchId("B1")
                .branchName("Sucursal Norte")
                .build();

        FranchiseModel franchise = FranchiseModel.builder()
                .franchiseId("F1")
                .branchModels(new ArrayList<>(List.of(existingBranch)))
                .build();

        when(validator.validateAndGetFranchiseByBranch("B1")).thenReturn(Mono.just(franchise));
        when(validator.getBranchOrThrow(franchise, "B1")).thenReturn(existingBranch);
        doNothing().when(validator).validateBranchNotExists(anyList(), eq(request));
        when(persistencePort.saveFranchise(franchise)).thenReturn(Mono.just(franchise));

        StepVerifier.create(useCase.updateBranch(request))
                .assertNext(updated -> {
                    assertEquals("B1", updated.getBranchId());
                    assertEquals("Sucursal Sur", updated.getBranchName());
                })
                .verifyComplete();

        verify(persistencePort).saveFranchise(franchise);
    }

    @Test
    void updateFranchise_success() {
        FranchiseModel request = FranchiseModel.builder()
                .franchiseId("F1")
                .franchiseName("Franquicia Premium")
                .build();

        FranchiseModel existing = FranchiseModel.builder()
                .franchiseId("F1")
                .franchiseName("Franquicia Centro")
                .build();

        when(validator.validateFranchiseNameNotExists("Franquicia Premium")).thenReturn(Mono.empty());
        when(validator.validateAndGetFranchise("F1")).thenReturn(Mono.just(existing));
        when(persistencePort.saveFranchise(existing)).thenReturn(Mono.just(existing));

        StepVerifier.create(useCase.updateFranchise(request))
                .assertNext(updated -> {
                    assertEquals("F1", updated.getFranchiseId());
                    assertEquals("Franquicia Premium", updated.getFranchiseName());
                })
                .verifyComplete();

        verify(persistencePort).saveFranchise(existing);
    }

    @Test
    void getTopStockProducts_success() {
        ProductModel p1 = ProductModel.builder().productId("P1").productName("A").stock(5).build();
        ProductModel p2 = ProductModel.builder().productId("P2").productName("B").stock(12).build();

        BranchModel branch = BranchModel.builder()
                .branchId("B1")
                .branchName("Sucursal Norte")
                .productModels(List.of(p1, p2))
                .build();

        FranchiseModel franchise = FranchiseModel.builder()
                .franchiseId("F1")
                .franchiseName("Franquicia Centro")
                .branchModels(List.of(branch))
                .build();

        when(validator.validateAndGetFranchise("F1")).thenReturn(Mono.just(franchise));

        StepVerifier.create(useCase.getTopStockProducts("F1"))
                .assertNext(list -> {
                    assertEquals(1, list.size());
                    assertEquals("P2", list.get(0).getProductId());
                    assertEquals("Sucursal Norte", list.get(0).getBranchName());
                })
                .verifyComplete();
    }

    @Test
    void deleteProduct_success() {
        ProductModel product = ProductModel.builder()
                .productId("P1")
                .productName("Coca Cola")
                .stock(10)
                .branchId("B1")
                .build();

        BranchModel branch = BranchModel.builder()
                .branchId("B1")
                .productModels(new ArrayList<>(List.of(product)))
                .build();

        FranchiseModel franchise = FranchiseModel.builder()
                .franchiseId("F1")
                .branchModels(new ArrayList<>(List.of(branch)))
                .build();

        when(validator.validateAndGetFranchiseByBranch("B1")).thenReturn(Mono.just(franchise));
        when(validator.getBranchOrThrow(franchise, "B1")).thenReturn(branch);
        when(validator.getProductOrThrow(branch.getProductModels(), "P1")).thenReturn(product);
        when(persistencePort.saveFranchise(franchise)).thenReturn(Mono.just(franchise));

        StepVerifier.create(useCase.deleteProduct("P1", "B1"))
                .verifyComplete();

        assertTrue(branch.getProductModels().isEmpty());
        verify(persistencePort).saveFranchise(franchise);
    }
}