package com.accenture.ms.franchise.service.infrastructure.entrypoints;

import com.accenture.ms.franchise.service.infrastructure.entrypoints.dto.request.ProductRequestDTO;
import com.accenture.ms.franchise.service.infrastructure.entrypoints.dto.request.ProductRequestUpdateDTO;
import com.accenture.ms.franchise.service.infrastructure.entrypoints.dto.response.ProductResponseDTO;
import com.accenture.ms.franchise.service.infrastructure.entrypoints.handler.IProductHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductRest {

    private final IProductHandler productHandler;

    @Operation(summary = "Create a new Product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created", content = @Content),
            @ApiResponse(responseCode = "404", description = "Branch not found", content = @Content),
            @ApiResponse(responseCode = "409", description = "Product already exists", content = @Content)
    })
    @PostMapping
    public Mono<ResponseEntity<ProductResponseDTO>> saveProduct(
            @Valid @RequestBody ProductRequestDTO productRequestDTO) {
        return productHandler.saveProduct(productRequestDTO)
                .map(dto -> ResponseEntity.status(HttpStatus.CREATED).body(dto));
    }

    @Operation(summary = "Update a Product pf stock")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product update", content = @Content),
            @ApiResponse(responseCode = "404", description = "Branch not found", content = @Content),
            @ApiResponse(responseCode = "409", description = "Product already exists", content = @Content)
    })
    @PutMapping
    public Mono<ResponseEntity<ProductResponseDTO>> updateStockProduct(
            @Valid @RequestBody ProductRequestUpdateDTO productRequestUpdateDTO) {
        return productHandler.updateStockProduct(productRequestUpdateDTO)
                .map(dto -> ResponseEntity.status(HttpStatus.OK).body(dto));
    }
}
