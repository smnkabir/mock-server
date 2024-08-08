package com.nk.mock.dynamic.endpoint;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EndPointDto {
    @NotBlank
    private String title;
    @NotBlank
    private String path;
    @NotBlank
    private String method;
    private List<ResponseDto> responseList = new ArrayList<>();
}
