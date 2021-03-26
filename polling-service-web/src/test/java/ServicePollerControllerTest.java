import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.kry.servicepoller.ServicePollerController;
import com.kry.servicepoller.controller.ServiceController;
import com.kry.servicepoller.model.dto.ServiceDto;
import com.kry.servicepoller.service.ApiService;
import com.kry.servicepoller.service.exception.ControllerExceptionHandler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(SpringJUnit4ClassRunner.class)
public class ServicePollerControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private ServicePollerController servicePollerController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = standaloneSetup(servicePollerController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @Mock
    private ApiService apiService;


    @Test
    public void homePageTest() throws Exception {

        ServiceDto service = new ServiceDto();
        service.setServiceId(1L);
        service.setService_Url(".service/all");
        service.setName("getItem");

        when(apiService.updateApi(any())).thenReturn(service);

        ResultActions actions = mockMvc
                .perform(get("/")
                        //.headers(getDefaultHeaders())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(getRequestJsonFilter(service)));


        MvcResult mvcResult = actions.andDo(print()).andExpect(status().isOk()).andReturn();

        assertNotNull(mvcResult.getResponse());
    }



    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("appName", appName);
        return "index";
    }

    @Test
    public void home(){

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

    private String getRequestJsonFilter(Object request) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        SimpleFilterProvider filters = new SimpleFilterProvider();
        filters.setFailOnUnknownId(false);

        ObjectWriter objectWriterFilter = objectMapper.writer(filters);
        return objectWriterFilter.writeValueAsString(request);
    }
}
