package com.kry.servicepoller.service.exception;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class ApiError {

    @NotNull
    private String code;

    @NotNull
    private Integer reason;

    private String message;

    private Integer status;

    private String type;

    private String referenceError;

}
