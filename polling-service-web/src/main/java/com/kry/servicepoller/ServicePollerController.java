package com.kry.servicepoller;

import com.kry.servicepoller.model.ServiceModel;
import com.kry.servicepoller.model.ServicePollModel;
import com.kry.servicepoller.model.dto.ServiceDto;
import com.kry.servicepoller.service.ApiService;
import com.kry.servicepoller.service.ServicePollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class ServicePollerController {

    @Value("${spring.application.name}")
    String appName;

    @Autowired
    private ServicePollService servicePool;

    @Autowired
    private ApiService apiService;

    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("appName", appName);
        return "index";
    }

    @GetMapping("/service/all")
    @ResponseStatus(HttpStatus.OK)
    public String getServices(Model model, @RequestParam("page") Optional<Integer> page,
                              @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(10);

        Page<ServiceModel> servicePage = apiService.
                getPaginatedServices(PageRequest.of(currentPage - 1, pageSize));

        model.addAttribute("servicePage", servicePage);

        int totalPages = servicePage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("appName", appName);

        return "service_list";
    }

    @GetMapping("/poller")
    @ResponseStatus(HttpStatus.OK)
    public String servicePollerPage(Model model, @RequestParam("page") Optional<Integer> page,
                                    @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(10);

        Page<ServicePollModel> servicePage = servicePool.
                getPaginatedPollServices(PageRequest.of(currentPage - 1, pageSize));

        model.addAttribute("servicePage", servicePage);

        int totalPages = servicePage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("appName", appName);

        return "poll_services";
    }

    @RequestMapping("/new")
    public String newServicePage(Model model) {
        ServiceDto service = new ServiceDto();
        model.addAttribute("new_service", service);
        return "new_service";
    }

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    public String createService(@ModelAttribute("new_service") ServiceDto serviceDto) {
        apiService.createApi(serviceDto);
        return "create_info";
    }

    @PostMapping("/update")
    @ResponseStatus(HttpStatus.CREATED)
    public String updateService(@ModelAttribute("update_service") ServiceDto serviceDto) {
        apiService.updateApi(serviceDto);
        return "update_info";
    }

    @RequestMapping("/edit/{id}")
    public ModelAndView showEditProductPage(@PathVariable(name = "id") Long id) {
        ModelAndView mav = new ModelAndView("update_service");
        ServiceDto service = apiService.getService(id);
        mav.addObject("update_service", service);

        return mav;
    }

    @RequestMapping("/delete/{id}")
    public String deleteProduct(@PathVariable(name = "id") Long id) {
        apiService.deleteById(id);
        return "delete_info";
    }

    @RequestMapping("/users")
    public String getUsers(Model model) {
        ServiceDto service = new ServiceDto();
        model.addAttribute("user_info", service);
        return "user_info";
    }

}
