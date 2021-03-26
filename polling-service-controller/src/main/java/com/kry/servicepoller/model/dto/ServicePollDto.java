package com.kry.servicepoller.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class ServicePollDto {
    @JsonProperty("Id")
    private Long poolId;
    @JsonProperty("service")
    private ServiceDto service;
    @JsonProperty("result")
    private String result;
    @JsonProperty("time")
    private ZonedDateTime poolTime;
}
