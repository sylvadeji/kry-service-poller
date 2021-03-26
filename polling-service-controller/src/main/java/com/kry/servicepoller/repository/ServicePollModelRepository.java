package com.kry.servicepoller.repository;

import com.kry.servicepoller.model.ServiceModel;
import com.kry.servicepoller.model.ServicePollModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ServicePollModelRepository extends JpaRepository<ServicePollModel, Long>,
        JpaSpecificationExecutor<ServiceModel> {
}
