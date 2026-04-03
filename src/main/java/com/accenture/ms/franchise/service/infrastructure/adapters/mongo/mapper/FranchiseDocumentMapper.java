package com.accenture.ms.franchise.service.infrastructure.adapters.mongo.mapper;

import com.accenture.ms.franchise.service.domain.model.BranchModel;
import com.accenture.ms.franchise.service.domain.model.FranchiseModel;
import com.accenture.ms.franchise.service.domain.model.ProductModel;
import com.accenture.ms.franchise.service.infrastructure.adapters.mongo.document.BranchDocument;
import com.accenture.ms.franchise.service.infrastructure.adapters.mongo.document.FranchiseDocument;
import com.accenture.ms.franchise.service.infrastructure.adapters.mongo.document.ProductDocument;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FranchiseDocumentMapper {

    public FranchiseDocument toDocument(FranchiseModel model) {
        if (model == null) {
            return null;
        }

        FranchiseDocument document = new FranchiseDocument();
        document.setFranchiseId(model.getFranchiseId());
        document.setFranchiseName(model.getFranchiseName());
        document.setBranches(toBranchDocuments(model.getBranchModels()));
        return document;
    }

    public FranchiseModel toModel(FranchiseDocument document) {
        if (document == null) {
            return null;
        }

        FranchiseModel model = new FranchiseModel();
        model.setFranchiseId(document.getFranchiseId());
        model.setFranchiseName(document.getFranchiseName());
        model.setBranchModels(toBranchModels(document.getBranches()));
        return model;
    }

    private List<BranchDocument> toBranchDocuments(List<BranchModel> branchModels) {
        if (branchModels == null) {
            return new ArrayList<>();
        }

        return branchModels.stream()
                .map(branch -> {
                    BranchDocument doc = new BranchDocument();
                    doc.setBranchId(branch.getBranchId());
                    doc.setBranchName(branch.getBranchName());
                    doc.setProducts(toProductDocuments(branch.getProductModels()));
                    return doc;
                })
                .collect(java.util.stream.Collectors.toCollection(ArrayList::new));
    }

    private List<BranchModel> toBranchModels(List<BranchDocument> branchDocuments) {
        if (branchDocuments == null) {
            return new ArrayList<>();
        }

        return branchDocuments.stream()
                .map(doc -> {
                    BranchModel model = new BranchModel();
                    model.setBranchId(doc.getBranchId());
                    model.setBranchName(doc.getBranchName());
                    model.setProductModels(toProductModels(doc.getProducts()));
                    return model;
                })
                .collect(java.util.stream.Collectors.toCollection(ArrayList::new));
    }

    private List<ProductDocument> toProductDocuments(List<ProductModel> productModels) {
        if (productModels == null) {
            return new ArrayList<>();
        }

        return productModels.stream()
                .map(product -> {
                    ProductDocument doc = new ProductDocument();
                    doc.setProductId(product.getProductId());
                    doc.setProductName(product.getProductName());
                    doc.setStock(product.getStock());
                    return doc;
                })
                .collect(java.util.stream.Collectors.toCollection(ArrayList::new));
    }

    private List<ProductModel> toProductModels(List<ProductDocument> productDocuments) {
        if (productDocuments == null) {
            return new ArrayList<>();
        }

        return productDocuments.stream()
                .map(doc -> {
                    ProductModel model = new ProductModel();
                    model.setProductId(doc.getProductId());
                    model.setProductName(doc.getProductName());
                    model.setStock(doc.getStock());
                    return model;
                })
                .collect(java.util.stream.Collectors.toCollection(ArrayList::new));
    }
}