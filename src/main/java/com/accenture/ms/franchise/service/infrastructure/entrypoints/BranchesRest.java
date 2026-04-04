package com.accenture.ms.franchise.service.infrastructure.entrypoints;

import com.accenture.ms.franchise.service.infrastructure.entrypoints.dto.request.BranchRequestDTO;
import com.accenture.ms.franchise.service.infrastructure.entrypoints.dto.request.BranchUpdateRequestDTO;
import com.accenture.ms.franchise.service.infrastructure.entrypoints.dto.response.BranchResponseDTO;
import com.accenture.ms.franchise.service.infrastructure.entrypoints.handler.IBranchHandler;
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
@RequestMapping("/api/v1/branch")
@RequiredArgsConstructor
public class BranchesRest {

    private final IBranchHandler branchHandler;

    @Operation(summary = "Create a new Branch")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Branch created", content = @Content),
            @ApiResponse(responseCode = "404", description = "Franchise not found", content = @Content),
            @ApiResponse(responseCode = "409", description = "Branch already exists", content = @Content)
    })
    @PostMapping
    public Mono<ResponseEntity<BranchResponseDTO>> saveBranch(
            @Valid @RequestBody BranchRequestDTO branchRequestDTO) {
        return branchHandler.saveBranch(branchRequestDTO)
                .map(dto -> ResponseEntity.status(HttpStatus.CREATED).body(dto));
    }

    @Operation(summary = "Update the name of a branch")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Branch updated", content = @Content),
            @ApiResponse(responseCode = "404", description = "Branch not found", content = @Content),
            @ApiResponse(responseCode = "409", description = "Branch name already exists", content = @Content)
    })
    @PutMapping
    public Mono<ResponseEntity<BranchResponseDTO>> updateBranchName(
            @Valid @RequestBody BranchUpdateRequestDTO branchUpdateRequestDTO) {
        return branchHandler.updateBranchName(branchUpdateRequestDTO)
                .map(dto -> ResponseEntity.status(HttpStatus.OK).body(dto))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
