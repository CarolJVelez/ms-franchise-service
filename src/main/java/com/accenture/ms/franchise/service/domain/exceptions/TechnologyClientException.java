package com.accenture.ms.franchise.service.domain.exceptions;

import lombok.Getter;

@Getter
public class TechnologyClientException extends RuntimeException {

    private final String body;
    private final int status;

    public TechnologyClientException(String body, int status) {
        super("Error del micro de tecnologías: " + body + " (status: " + status + ")");
        this.body = body;
        this.status = status;
    }
}
