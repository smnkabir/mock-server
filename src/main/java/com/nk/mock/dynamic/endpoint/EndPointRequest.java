package com.nk.mock.dynamic.endpoint;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class EndPointRequest {
    @NotBlank
    private String title;
    @NotBlank
    private String path;
    @NotBlank
    private String method;
    private List<ResponseRequest> responseList = new ArrayList<>();
}
