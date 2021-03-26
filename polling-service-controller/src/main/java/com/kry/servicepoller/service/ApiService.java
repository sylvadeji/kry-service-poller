package com.kry.servicepoller.service;

import com.kry.servicepoller.model.ServiceModel;
import com.kry.servicepoller.model.dto.ServiceDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ApiService {
    public ServiceDto createApi(ServiceDto service);

    public List<ServiceDto> getAllServices();

    public Page<ServiceModel> getPaginatedServices(Pageable pageable);

    public ServiceDto getService(Long id);

    public ServiceDto updateApi(ServiceDto service);

    public void deleteById(Long id);

    public void deleteApi(ServiceDto service);
}
