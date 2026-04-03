package com.accenture.ms.franchise.service.infrastructure.entrypoints;

import com.accenture.ms.franchise.service.infrastructure.entrypoints.dto.request.FranchiseRequestDTO;
import com.accenture.ms.franchise.service.infrastructure.entrypoints.dto.response.FranchiseResponseDTO;
import com.accenture.ms.franchise.service.infrastructure.entrypoints.handler.IFranchiseHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/franchises")
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
}
