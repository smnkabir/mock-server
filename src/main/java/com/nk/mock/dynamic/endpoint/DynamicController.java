package com.nk.mock.dynamic.endpoint;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> storeNewEndPoint(@RequestBody @Valid EndPointRequest request) {
        return service.storeNewEndPoint(request);
    }

    @PostMapping("/response")
    public ResponseEntity<?> addNewResponse(@RequestBody @Valid ResponseRequest request) {
        return service.storeNewResponse(request);
    }

    @DeleteMapping("/response")
    public ResponseEntity<?> removeResponse(@RequestBody @Valid ResDeleteRequest request) {
        return service.removeRes(request);
    }

    @PutMapping("/response")
    public ResponseEntity<?> updateResponseStatus(@RequestBody ResponseStatusUpdateRequest request) {
        return service.updateResponseStatus(request);
    }
}
