package com.kry.servicepoller.service;

import com.kry.servicepoller.model.ServicePollModel;
import com.kry.servicepoller.model.dto.ServicePollDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ServicePollService {
    public void poolService();

    public Long getDelay();

    public List<ServicePollDto> getAllPollServices();

    public Page<ServicePollModel> getPaginatedPollServices(Pageable pageable);
}
