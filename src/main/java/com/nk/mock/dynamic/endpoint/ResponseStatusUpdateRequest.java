package com.nk.mock.dynamic.endpoint;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResponseStatusUpdateRequest {
    @NotNull
    private Long endpointId;
    @NotNull
    private Long resId;
}
