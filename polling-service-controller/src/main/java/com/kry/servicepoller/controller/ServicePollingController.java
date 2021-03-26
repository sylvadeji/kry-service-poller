package com.kry.servicepoller.controller;

import com.kry.servicepoller.model.dto.ServicePollDto;
import com.kry.servicepoller.service.ServicePollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/availability")
public class ServicePollingController {

    @Autowired
    ServicePollService servicePoll;

    @GetMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<ServicePollDto> getPollServices() {
        return servicePoll.getAllPollServices();
    }

}
