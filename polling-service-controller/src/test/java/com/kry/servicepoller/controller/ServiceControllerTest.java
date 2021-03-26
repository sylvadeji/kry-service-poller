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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(SpringJUnit4ClassRunner.class)
public class ServiceControllerTest {

    private MockMvc mockMvc;

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

/*    @Test
    public void testListResourcesforMultipleIds() throws  Exception {
        Map<String, String> p = new HashMap<>();
        p.put("id", "231,232");
        ResponseEntity<MappingJacksonValue> actual =
                resourceController.getResourceList("", null, null,"", "", 2, 2, "", p, false, null);
        assertEquals(HttpStatus.OK, actual.getStatusCode());
    }*/


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

  /*  ResultActions actions = mockMvc
            .perform(
                    post("/v1/resource/assign")
                            .headers(getDefaultHeaders())
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .characterEncoding("UTF-8")
                            .content(getRequestJsonFilter(resource)));

    MvcResult mvcResult = actions.andDo(print()).andExpect(status().isOk()).andReturn();*/

    //assertNotNull(mvcResult.getResponse());

    private String getRequestJsonFilter(Object request) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        SimpleFilterProvider filters = new SimpleFilterProvider();
        filters.setFailOnUnknownId(false);

        ObjectWriter objectWriterFilter = objectMapper.writer(filters);
        return objectWriterFilter.writeValueAsString(request);
    }
}
