package com.nk.mock.dynamic.endpoint;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResDeleteRequest {
    @NotNull
    private Long resId;
}
