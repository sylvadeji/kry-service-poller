package com.kry.servicepoller.model.mapper;

import com.kry.servicepoller.model.ServicePollModel;
import com.kry.servicepoller.model.dto.ServicePollDto;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ServicePollMapper {
    @Autowired
    ModelMapper mapper;

    public ServicePollDto mapFromModel(ServicePollModel servicePoolModel) {
        ServicePollDto servicePoolDto = new ServicePollDto();
        try {
            servicePoolDto = mapper.map(servicePoolModel, ServicePollDto.class);
        } catch (Exception ex) {
            log.error("Object Mappping Error occurred");
            return servicePoolDto;
        }
        return servicePoolDto;
    }
}
