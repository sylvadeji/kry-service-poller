package com.kry.servicepoller.controller;

import com.kry.servicepoller.model.dto.ServiceDto;
import com.kry.servicepoller.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/services")
public class ServiceController {

    public static final String ID = "id";
    public static final String X_TRANSACTION_ID = "Transaction-Id";

    @Autowired
    private ApiService apiService;

    @GetMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<ServiceDto> getAllServices() {
        return apiService.getAllServices();
    }

    @GetMapping("{" + ID + "}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ServiceDto getService(@PathVariable(name = ID) Long id) {
        return apiService.getService(id);
    }

    @PostMapping("/create")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public ServiceDto createService(@RequestHeader(name = X_TRANSACTION_ID, required = false)
                                                String headerXTransactionId,
                                    @RequestBody ServiceDto serviceDto) {
        return apiService.createApi(serviceDto);
    }

    @PutMapping("/update")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ServiceDto> updateService(@RequestBody ServiceDto serviceDto) {
        return ResponseEntity.ok(apiService.updateApi(serviceDto));
    }

    @DeleteMapping("/delete")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public void deleteService(@RequestBody ServiceDto serviceDto) {
        apiService.deleteApi(serviceDto);
    }
}
