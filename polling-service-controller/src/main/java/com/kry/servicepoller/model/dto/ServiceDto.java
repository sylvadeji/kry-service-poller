package com.kry.servicepoller.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class ServiceDto {
    @JsonProperty("id")
    private Long serviceId;
    @JsonProperty("url")
    private String service_Url;
    @JsonProperty("name")
    private String name;
    @JsonProperty("dateCreated")
    private ZonedDateTime dateCreated;
    @JsonProperty("lastDateModified")
    private ZonedDateTime lastDateModified;
    @JsonProperty("user")
    private UserDto user;
    @JsonProperty("status")
    private int status;
}
