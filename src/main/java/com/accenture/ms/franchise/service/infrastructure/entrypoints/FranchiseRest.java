package com.accenture.ms.franchise.service.infrastructure.entrypoints;

import com.accenture.ms.franchise.service.infrastructure.entrypoints.dto.request.FranchiseRequestDTO;
import com.accenture.ms.franchise.service.infrastructure.entrypoints.dto.request.FranchiseUpdateRequestDTO;
import com.accenture.ms.franchise.service.infrastructure.entrypoints.dto.response.FranchiseResponseDTO;
import com.accenture.ms.franchise.service.infrastructure.entrypoints.dto.response.ProductBranchResponseDTO;
import com.accenture.ms.franchise.service.infrastructure.entrypoints.handler.IFranchiseHandler;
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

import java.util.List;

@RestController
@RequestMapping("/api/v1/franchise")
@RequiredArgsConstructor
public class FranchiseRest {

    private final IFranchiseHandler franchiseHandler;

    @Operation(summary = "Create a new Franchise")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Franchise created", content = @Content),
            @ApiResponse(responseCode = "409", description = "Franchise already exists", content = @Content)
    })
    @PostMapping
    public Mono<ResponseEntity<FranchiseResponseDTO>> saveFranchise(
            @Valid @RequestBody FranchiseRequestDTO franchiseRequestDTO) {
        return franchiseHandler.saveFranchise(franchiseRequestDTO)
                .map(dto -> ResponseEntity.status(HttpStatus.CREATED).body(dto));
    }

    @Operation(summary = "Get product with highest stock per branch for a franchise")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of products with highest stock per branch", content = @Content),
            @ApiResponse(responseCode = "404", description = "Franchise not found", content = @Content)
    })
    @GetMapping("/{franchiseId}/top-stock")
    public Mono<ResponseEntity<List<ProductBranchResponseDTO>>> getTopStockProducts(
            @PathVariable String franchiseId) {
        return franchiseHandler.getTopStockProducts(franchiseId)
                .map(ResponseEntity::ok);
    }

    @Operation(summary = "Update the name of a franchise")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Franchise updated", content = @Content),
            @ApiResponse(responseCode = "404", description = "Franchise not found", content = @Content),
            @ApiResponse(responseCode = "409", description = "Franchise name already exists", content = @Content)
    })
    @PutMapping
    public Mono<ResponseEntity<FranchiseResponseDTO>> updateFranchiseName(
            @Valid @RequestBody FranchiseUpdateRequestDTO franchiseUpdateRequestDTO) {
        return franchiseHandler.updateFranchiseName(franchiseUpdateRequestDTO)
                .map(dto -> ResponseEntity.status(HttpStatus.OK).body(dto))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
