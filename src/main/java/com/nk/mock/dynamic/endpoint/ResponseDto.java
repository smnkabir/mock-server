package com.nk.mock.dynamic.endpoint;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto {
    private Long id;
    private Long endPointId;
    @NotNull
    private int statusCode;
    @NotBlank
    private String res;
    private int status;

    public ResponseDto(EndPointRes endPointRes) {
        id = endPointRes.getId();
        endPointId = endPointRes.getEndPoint().getId();
        statusCode = endPointRes.getStatusCode();
        res = endPointRes.getRes();
        status = endPointRes.getStatus();
    }
}
