package com.nk.mock.dynamic.endpoint;

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
    public ResponseEntity<?> getEndpoints() {
        return service.getEndPoint();
    }

    @GetMapping("")
    public ResponseEntity<?> getEndpoint(@RequestParam(value = "id") Long id) {
        return service.getEndPoint(id);
    }

    @PostMapping("")
    public ResponseEntity<?> storeNewEndPoint(@RequestBody @Valid EndPointDto request) {
        return service.storeNewEndPoint(request);
    }

    @PostMapping("/response")
    public ResponseEntity<?> addNewResponse(@RequestBody @Valid ResponseDto request) {
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
