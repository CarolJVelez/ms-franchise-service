package com.accenture.ms.franchise.service.infrastructure.adapters.mongo.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "franchises")
public class FranchiseDocument {

    @Id
    private String franchiseId;
    private String franchiseName;
    @Builder.Default
    private List<BranchDocument> branches = new ArrayList<>();
}
