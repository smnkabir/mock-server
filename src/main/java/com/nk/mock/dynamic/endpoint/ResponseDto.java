package com.nk.mock.dynamic.endpoint;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResponseDto {
    private Long endPointId;
    @NotNull
    private int statusCode;
    @NotBlank
    private String res;
    private int status;

    public ResponseDto(EndPointRes endPointRes) {
        endPointId = endPointRes.getId();
        statusCode = endPointRes.getStatusCode();
        res = endPointRes.getRes();
        status = endPointRes.getStatus();
    }
}
