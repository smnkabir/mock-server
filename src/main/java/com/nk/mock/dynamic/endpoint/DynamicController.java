package com.nk.mock.dynamic.endpoint;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/config")
public class DynamicController {
    private final DynamicControllerService service;

    public DynamicController(DynamicControllerService service) {
        this.service = service;
    }

    @GetMapping("")
    public ResponseEntity<?> getRes(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return service.getEndPoint();
    }

    @PostMapping("")
    public ResponseEntity<?> storeNewEndPoint(@RequestBody @Validated EndPointRequest request) {
        return service.storeNewEndPoint(request);
    }

    @PostMapping("/response")
    public ResponseEntity<?> addNewResponse(@RequestBody @Validated ResponseRequest request) {
        return service.storeNewResponse(request);
    }

    @PutMapping("/response")
    public ResponseEntity<?> updateResponseStatus(@RequestBody @Validated ResponseStatusUpdateRequest request) {
        return service.updateResponseStatus(request);
    }
}
