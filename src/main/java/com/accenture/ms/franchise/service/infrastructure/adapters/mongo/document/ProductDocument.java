package com.accenture.ms.franchise.service.infrastructure.adapters.mongo.document;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDocument {
    private String productId;
    private String productName;
    private Integer stock;
}
