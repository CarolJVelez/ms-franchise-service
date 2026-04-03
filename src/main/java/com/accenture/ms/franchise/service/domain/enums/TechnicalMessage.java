package com.accenture.ms.franchise.service.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TechnicalMessage {

    INTERNAL_ERROR("500", "Something went wrong, please try again", ""),
    INVALID_REQUEST("400", "Bad Request, please verify data", ""),
    FRANCHISE_NAME_REQUIRED("400", "El nombre de la franquicia es obligatorio.", "franchiseName"),
    FRANCHISE_ID_REQUIRED("400", "El ID de la franquicia es obligatorio.", "franchiseId"),
    BRANCH_NAME_REQUIRED("400", "El nombre de la sucursal es obligatorio.", "branchName"),
    BRANCH_ID_REQUIRED("400", "El ID de la sucursal es obligatorio.", "branchId"),
    BRANCH_NAME_ALREADY_EXISTS("409", "El nombre de la sucursal ya existe.", "branchName"),
    PRODUCT_NAME_REQUIRED("400", "El nomobre del producto es obligatorio.", "productName"),
    STOCK_REQUIRED("400", "La cantidad del stock del producto debe de ser mayor a cero.", "stock"),
    FRANCHISE_NOT_FOUND("404", "La Franquicia no existe.", "franchiseName"),
    BRANCH_NOT_FOUND("404", "La Sucursal no existe.", "branchName"),
    FRANCHISE_NAME_ALREADY_EXISTS("409", "El nombre de la franquicia ya existe.", "franchiseName"),
    PRODUCT_NAME_ALREADY_EXISTS("409", "El nombre del producto ya existe.", "productName"),
    PRODUCT_NOT_FOUND("404", "El producto no existe.", "productName"),;

    private final String code;
    private final String message;
    private final String param;
}