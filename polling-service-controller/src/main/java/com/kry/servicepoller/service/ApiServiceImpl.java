package com.kry.servicepoller.service;

import com.kry.servicepoller.model.ServiceModel;
import com.kry.servicepoller.model.dto.ServiceDto;
import com.kry.servicepoller.model.mapper.ServiceMapper;
import com.kry.servicepoller.model.mapper.UserMapper;
import com.kry.servicepoller.repository.ServiceModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

/***
 * Service To manage all Services
 */

@Service
public class ApiServiceImpl implements ApiService {
    @Autowired
    private ServiceMapper serviceMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ServiceModelRepository serviceModelRepository;

    @Override
    public ServiceDto createApi(ServiceDto service) {
        ServiceDto serviceDto = new ServiceDto();
        //Validate Service
        if (validateService(service)) {
            //Map Service to Model
            ServiceModel persistableService = serviceMapper.mapToModel(service);
            // Save Service
            serviceModelRepository.save(persistableService);
            //Map Model to Service
            serviceDto = serviceMapper.mapFromModel(persistableService);
        }
        return serviceDto;
    }

    @Override
    public List<ServiceDto> getAllServices() {
        List<ServiceDto> dto = new ArrayList<ServiceDto>();
        List<ServiceModel> allServices = serviceModelRepository.findAll();

        dto = allServices.stream().map(service -> serviceMapper.mapFromModel(service))
                .collect(Collectors.toList());

        return dto;
    }

    @Override
    public Page<ServiceModel> getPaginatedServices(Pageable pageable) {

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<ServiceModel> list;

        List<ServiceModel> serviceModels = serviceModelRepository.findAll();

        if (serviceModels.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, serviceModels.size());
            list = serviceModels.subList(startItem, toIndex);
        }

        return new PageImpl<ServiceModel>(list, PageRequest.of(currentPage, pageSize), serviceModels.size());

    }

    @Override
    public ServiceDto getService(Long id) {
        ServiceDto dto = new ServiceDto();
        Optional<ServiceModel> service = serviceModelRepository.findById(id);

        if (service.isPresent()) {
            dto = serviceMapper.mapFromModel(service.get());
        }

        return dto;
    }

    @Override
    public ServiceDto updateApi(ServiceDto service) {
        if (Objects.nonNull(service) && Objects.nonNull(service.getServiceId())) {
            Optional<ServiceModel> availableService =
                    serviceModelRepository.findById(service.getServiceId());
            if (availableService.isPresent()) {
                //update
                ServiceModel newService = availableService.get();
                newService.setService_Url(service.getService_Url());
                newService.setName(service.getName());
                newService.setLastDateModified(ZonedDateTime.now());
                newService.setStatus(service.getStatus() == 1 ? 1 : 0);
                serviceModelRepository.saveAndFlush(newService);

                return serviceMapper.mapFromModel(newService);
            }

        }
        return null;
    }

    @Override
    public void deleteApi(ServiceDto service) {
        if (Objects.nonNull(service) && Objects.nonNull(service.getServiceId())) {

            Optional<ServiceModel> availableService =
                    serviceModelRepository.findById(service.getServiceId());
            if (availableService.isPresent()) {
                //Delete
                serviceModelRepository.delete(availableService.get());
            }
        }
    }

    @Override
    public void deleteById(Long id) {
        serviceModelRepository.deleteById(id);
    }

    private boolean validateService(ServiceDto serviceDto) {
        return Objects.nonNull(serviceDto) && Objects.nonNull(serviceDto.getName())
                && Objects.nonNull(serviceDto.getService_Url());
    }


}
