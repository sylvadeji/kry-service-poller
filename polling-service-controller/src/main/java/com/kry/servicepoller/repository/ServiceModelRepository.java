package com.kry.servicepoller.repository;

import com.kry.servicepoller.model.ServiceModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ServiceModelRepository extends JpaRepository<ServiceModel, Long>,
        JpaSpecificationExecutor<ServiceModel> {
}
