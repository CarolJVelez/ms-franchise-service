package com.accenture.ms.franchise.service.domain.exceptions;

import com.accenture.ms.franchise.service.domain.enums.TechnicalMessage;
import lombok.Getter;

@Getter
public class BusinessException extends ProcessorException {

    public BusinessException(TechnicalMessage technicalMessage) {
        super(technicalMessage.getMessage(), technicalMessage);
    }

    public BusinessException(TechnicalMessage technicalMessage, String detail) {
        super(technicalMessage.getMessage() + " - " + detail, technicalMessage);
    }


}
