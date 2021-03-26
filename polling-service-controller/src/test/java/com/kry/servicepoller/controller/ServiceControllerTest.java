package com.kry.servicepoller.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.kry.servicepoller.model.dto.ServiceDto;
import com.kry.servicepoller.service.ApiService;
import com.kry.servicepoller.service.exception.ControllerExceptionHandler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(SpringJUnit4ClassRunner.class)
public class ServiceControllerTest {

    private MockMvc mockMvc;

    private static final String X_TRANSACTION_ID = "Transaction-Id";

    @InjectMocks
    private ServiceController serviceController;

    @Mock
    private ApiService apiService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = standaloneSetup(serviceController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @Test
    public void getAllServicesTest() throws Exception {
        List<ServiceDto> services = new ArrayList<ServiceDto>();

        ServiceDto oService = new ServiceDto();
        oService.setServiceId(1L);
        oService.setService_Url(".service/all");
        oService.setName("getAll");

        services.add(oService);

        when(apiService.getAllServices()).thenReturn(services);

        List<ServiceDto> actual = serviceController.getAllServices();

        assertEquals(services, actual);
    }

    @Test
    public void getSingleServiceItemTest() throws Exception {

        ServiceDto service = new ServiceDto();
        service.setServiceId(1L);
        service.setService_Url(".service/all");
        service.setName("getItem");

        when(apiService.getService(anyLong())).thenReturn(service);

        ServiceDto actual = serviceController.getService(1L);

        assertEquals(service, actual);
    }

    @Test
    public void createServiceItemTest() throws Exception {

        ServiceDto service = new ServiceDto();
        service.setServiceId(1L);
        service.setService_Url(".service/all");
        service.setName("getItem");

        when(apiService.createApi(any())).thenReturn(service);

        ResultActions actions = mockMvc
                .perform(
                        post("/services/create")
                                .headers(getDefaultHeaders())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                                .content(getRequestJsonFilter(service)));

        MvcResult mvcResult = actions.andDo(print()).andExpect(status().isCreated()).andReturn();

        assertNotNull(mvcResult.getResponse());
    }

    private String getRequestJsonFilter(Object request) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        SimpleFilterProvider filters = new SimpleFilterProvider();
        filters.setFailOnUnknownId(false);

        ObjectWriter objectWriterFilter = objectMapper.writer(filters);
        return objectWriterFilter.writeValueAsString(request);
    }

    private HttpHeaders getDefaultHeaders() {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(X_TRANSACTION_ID, "test");
        return httpHeaders;
    }
}
