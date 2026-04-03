package com.accenture.ms.franchise.service.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ProductBranchModel {
    private String productId;
    private String productName;
    private Integer stock;
    private String branchId;
    private String branchName;
    private String franchiseId;
    private String franchiseName;

}
