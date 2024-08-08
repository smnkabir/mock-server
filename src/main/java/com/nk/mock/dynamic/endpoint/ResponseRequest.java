package com.nk.mock.dynamic.endpoint;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResponseRequest {
    private Long endPointId;
    @NotNull
    private int statusCode;
    @NotBlank
    private String res;
}
