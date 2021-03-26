package com.kry.servicepoller.model.mapper;

import com.kry.servicepoller.model.ServiceModel;
import com.kry.servicepoller.model.UserModel;
import com.kry.servicepoller.model.dto.ServiceDto;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Objects;

@Service
@Slf4j
public class ServiceMapper {

    @Autowired
    ModelMapper mapper;

    @Autowired
    UserMapper userMapper;

    public ServiceModel mapToModel(ServiceDto serviceDto) {
        ServiceModel serviceModel = mapper.map(serviceDto, ServiceModel.class);
        if (Objects.isNull(serviceDto.getUser())) {
            UserModel oUser = new UserModel();
            oUser.setUserId(1L);
            serviceModel.setUser(oUser);
        }
        serviceModel.setDateCreated(Objects.isNull(serviceDto.getDateCreated()) ?
                ZonedDateTime.now() : serviceDto.getDateCreated());
        serviceModel.setLastDateModified(Objects.isNull(serviceDto.getLastDateModified()) ?
                ZonedDateTime.now() : serviceDto.getLastDateModified());
        return serviceModel;
    }

    public ServiceDto mapFromModel(ServiceModel serviceModel) {
        return mapFromModelManual(serviceModel);
    }

    public ServiceDto mapFromModelManual(ServiceModel serviceModel) {
        ServiceDto serviceDto = new ServiceDto();
        try {
            serviceDto.setDateCreated(serviceModel.getDateCreated());
            serviceDto.setLastDateModified(serviceModel.getLastDateModified());
            serviceDto.setName(serviceModel.getName());
            serviceDto.setService_Url(serviceModel.getService_Url());
            serviceDto.setServiceId(serviceModel.getServiceId());
            serviceDto.setStatus(serviceModel.getStatus());
            serviceDto.setUser(Objects.nonNull(serviceModel.getUser()) ?
                    userMapper.mapFromModel(serviceModel.getUser()) : null);
        } catch (Exception ex) {
            log.error("Object Mappping Error occurred");
            return serviceDto;
        }
        return serviceDto;
    }
}
