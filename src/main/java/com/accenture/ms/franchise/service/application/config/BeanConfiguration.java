package com.accenture.ms.franchise.service.application.config;

import com.accenture.ms.franchise.service.domain.api.IFranchiseServicePort;
import com.accenture.ms.franchise.service.domain.spi.IFranchisePersistencePort;
import com.accenture.ms.franchise.service.domain.usecase.FranchiseUseCase;
import com.accenture.ms.franchise.service.domain.validation.FranchiseValidator;
import com.accenture.ms.franchise.service.infrastructure.adapters.mongo.adapter.FranchiseMongoAdapter;
import com.accenture.ms.franchise.service.infrastructure.adapters.mongo.mapper.FranchiseDocumentMapper;
import com.accenture.ms.franchise.service.infrastructure.adapters.mongo.repository.IFranchiseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    private final IFranchiseRepository franchiseRepository;
    private final FranchiseDocumentMapper franchiseDocumentMapper;

    @Bean
    public IFranchisePersistencePort franchisePersistencePort() {
        return new FranchiseMongoAdapter(franchiseRepository, franchiseDocumentMapper);
    }

    @Bean
    public FranchiseValidator franchiseValidator() {
        return new FranchiseValidator(franchisePersistencePort());
    }

    @Bean
    public IFranchiseServicePort franchiseServicePort() {
        return new FranchiseUseCase(
                franchisePersistencePort(),
                franchiseValidator()
        );
    }
}
