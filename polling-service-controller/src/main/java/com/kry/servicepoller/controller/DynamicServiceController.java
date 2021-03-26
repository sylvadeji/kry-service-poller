package com.kry.servicepoller.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

/***
 * Dynamic Service Controller
 * This is just a test to ensure that created services
 * have endpoints
 */

@RestController
@RequestMapping("/dynamic")
public class DynamicServiceController {

    @GetMapping("/getNumbers")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public String getRandomNumbers() {
        Random rnd = new Random(10);
        return rnd.nextInt(35) + "12345678";
    }

    @GetMapping("/getTest")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public String getTest() {
        return "Test Seems to be working";
    }

    @GetMapping("/getOrders")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public String getCustomerOrders() {
        return "1. Just a Single Order available";
    }

}
