import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.kry.servicepoller.ServicePollerController;
import com.kry.servicepoller.controller.ServiceController;
import com.kry.servicepoller.model.ServiceModel;
import com.kry.servicepoller.model.dto.ServiceDto;
import com.kry.servicepoller.repository.ServiceModelRepository;
import com.kry.servicepoller.service.ApiService;
import com.kry.servicepoller.service.exception.ControllerExceptionHandler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @Mock
    ServiceModelRepository serviceModelRepository;


    @Test
    public void homePageTest() throws Exception {
        ResultActions actions = mockMvc
                .perform(get("/")
                        //.headers(getDefaultHeaders())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"));
        // .content(getRequestJsonFilter(service)));


        MvcResult mvcResult = actions.andDo(print()).andExpect(status().isOk()).andReturn();

        assertNotNull(mvcResult.getResponse());
    }

    @Test
    public void getServicesPageTest() throws Exception {
        Optional<Integer> page = Optional.of(1);
        Optional<Integer> size = Optional.of(10);

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(10);

        ServiceModel oModel = new ServiceModel();
        oModel.setName("getNumber");

        List<ServiceModel> serviceModels = new ArrayList<ServiceModel>();

        serviceModels.add(oModel);

        when(serviceModelRepository.findAll()).thenReturn(serviceModels);

        Page<ServiceModel> pages;

        pages = new PageImpl<ServiceModel>(serviceModels,
                PageRequest.of(currentPage, pageSize), serviceModels.size());

        when(apiService.getPaginatedServices(any())).thenReturn(pages);

        ResultActions actions = mockMvc
                .perform(get("/service/all")
                        //.headers(getDefaultHeaders())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(getRequestJsonFilter(serviceModels)));

        MvcResult mvcResult = actions.andDo(print()).andExpect(status().isOk()).andReturn();

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
}
