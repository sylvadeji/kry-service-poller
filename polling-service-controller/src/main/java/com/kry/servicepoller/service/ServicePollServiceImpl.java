package com.kry.servicepoller.service;

import com.kry.servicepoller.model.ServicePollModel;
import com.kry.servicepoller.model.dto.ServiceDto;
import com.kry.servicepoller.model.dto.ServicePollDto;
import com.kry.servicepoller.model.mapper.ServiceMapper;
import com.kry.servicepoller.model.mapper.ServicePollMapper;
import com.kry.servicepoller.repository.ServicePollModelRepository;
import com.kry.servicepoller.service.exception.ServiceNotAvailableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ServicePollServiceImpl implements ServicePollService {
    private static final String FAIL = "FAIL";
    private static final String OK = "OK";

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ApiService apiService;

    @Autowired
    private ServiceMapper serviceMapper;

    @Autowired
    private ServicePollMapper servicePoolMapper;

    @Autowired
    private ServicePollModelRepository servicePoolModelRepository;

    @Value("${fixedDelay.in.milliseconds:3000}")
    private Long fixDelay;

    @Override
    //@Scheduled(cron = "0 0/1 * * * ?", zone = "Europe/Berlin")
    //@Scheduled(cron = "${cron.expression}", zone = "Europe/Berlin")
    public void poolService() {
        List<ServiceDto> dto = new ArrayList<ServiceDto>();
        //Fetch The Service list from API Service
        List<ServiceDto> serviceList = apiService.getAllServices();

        //Do HttpRest on each one
        serviceList.parallelStream().forEach(v -> {
            try {
                //Save Result back to DB
                saveResponse(v, getServiceAvailability(v));
            } catch (ServiceNotAvailableException e) {
                log.error(e.getMessage());
            }
        });
    }

    @Override
    public Long getDelay() {
        return fixDelay;
    }

    @Override
    public List<ServicePollDto> getAllPollServices() {
        List<ServicePollDto> dto;
        Pageable topTwenty = PageRequest.of(0, 20, Sort.Direction.DESC, "poolId");

        Page<ServicePollModel> allPools = servicePoolModelRepository.findAll(topTwenty);

        dto = allPools.stream().map(x -> servicePoolMapper.mapFromModel(x))
                .collect(Collectors.toList());

        return dto;
    }

    public Page<ServicePollModel> getPaginatedPollServices(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<ServicePollModel> list;

        List<ServicePollModel> allPools = servicePoolModelRepository.findAll();

        if (allPools.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, allPools.size());
            list = allPools.subList(startItem, toIndex);
        }
        return new PageImpl<ServicePollModel>(list, PageRequest.of(currentPage, pageSize), allPools.size());
    }

    private String getServiceAvailability(ServiceDto service) throws ServiceNotAvailableException {
        String response = FAIL;
        //Check if Service status is false and return fail immediately
        if (service.getStatus() < 1) {
            return response;
        }

        //Set Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Transaction-Id", UUID.randomUUID().toString());
        HttpEntity<?> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(service.getService_Url(),
                    HttpMethod.GET, entity, String.class);

            String result = Optional.of(responseEntity).map(ResponseEntity::getBody)
                    .orElse(response);

            if (!response.equalsIgnoreCase(result)) {
                response = OK;
            }
        } catch (RestClientException e) {
            String errorMsg = String.format("Service {} " + FAIL, service.getName());
            log.error(errorMsg);
            throw new ServiceNotAvailableException(errorMsg.replace("{}", service.getName()), e);
        }

        return response;
    }

    private void saveResponse(ServiceDto service, String result) {

        ServicePollModel poolModel = new ServicePollModel();
        poolModel.setPoolTime(ZonedDateTime.now());
        poolModel.setResult(result);
        //Map Response to Model
        poolModel.setService(serviceMapper.mapToModel(service));
        //Save to DB
        servicePoolModelRepository.save(poolModel);
        log.info("Response saved for Service {}", service.getServiceId());

    }

}
