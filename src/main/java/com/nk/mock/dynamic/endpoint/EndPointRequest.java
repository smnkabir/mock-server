package com.nk.mock.dynamic.endpoint;

import lombok.Data;

import java.util.List;

@Data
public class EndPointRequest {
    private String title;
    private String path;
    private String method;
    private List<ResponseRequest> responseList;
}
