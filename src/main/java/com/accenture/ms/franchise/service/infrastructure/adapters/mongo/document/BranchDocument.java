package com.accenture.ms.franchise.service.infrastructure.adapters.mongo.document;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BranchDocument {
    private String branchId;
    private String branchName;
    @Builder.Default
    private List<ProductDocument> products = new ArrayList<>();
}
