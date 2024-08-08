package com.nk.mock.dynamic.endpoint;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.*;

@Service
public class DynamicControllerService {
    private final EndPointRepository endPointRepository;
    private final EndPointResRepository resRepository;
    private final Map<String, EndPoint> endPointMap = new HashMap<>();
    private final RequestMappingHandlerMapping handlerMapping;

    public DynamicControllerService(EndPointRepository endPointRepository, EndPointResRepository resRepository, RequestMappingHandlerMapping handlerMapping) {
        this.endPointRepository = endPointRepository;
        this.resRepository = resRepository;
        this.handlerMapping = handlerMapping;
    }

    @PostConstruct
    public void init() {
        List<EndPoint> all = endPointRepository.findAll();
        all.forEach(endPoint -> {
            try {
                registerEndpoint(endPoint);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            endPointMap.put(getPathKey(endPoint.getPath(), endPoint.getMethod()), endPoint);
        });
    }

    String getPathKey(String uri, String method) {
        return uri + "_" + method;
    }

    private void readAndStoreData(String method, HttpServletRequest request) throws IOException {
        InputStream inputStream = request.getInputStream();
        StringBuilder stringBuilder = new StringBuilder();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            stringBuilder.append(new String(buffer, 0, bytesRead));
        }

    }

    public void registerEndpoint(EndPoint endpoint) throws Exception {
        Method method = this.getClass().getMethod("handleRequest", HttpServletRequest.class);
        RequestMappingInfo requestMappingInfo = RequestMappingInfo.paths(endpoint.getPath())
                .methods(RequestMethod.valueOf(endpoint.getMethod()))
                .build();
        handlerMapping.registerMapping(requestMappingInfo, this, method);
    }

    public ResponseEntity<?> handleRequest(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();

        EndPoint endPoint = endPointMap.get(getPathKey(path, method));
        if (endPoint == null)
            return new ResponseEntity<>("Invalid Request Url: " + path, HttpStatus.NOT_FOUND);

        Optional<EndPointRes> endPointResOpt = resRepository.findByEndPointAndStatus(endPoint, 1);
        if (endPointResOpt.isEmpty())
            return new ResponseEntity<>("Invalid Request : Response not configured" + path, HttpStatus.BAD_REQUEST);

        EndPointRes endPointRes = endPointResOpt.get();
        return new ResponseEntity<>(endPointRes.getRes(), HttpStatus.valueOf(endPointRes.getStatusCode()));

    }

    public ResponseEntity<?> updateResponseStatus(ResponseStatusUpdateRequest request) {
        Optional<EndPoint> endPointOpt = endPointRepository.findById(request.getEndpointId());
        if (endPointOpt.isEmpty())
            return new ResponseEntity<>("Invalid Endpoint ID", HttpStatus.NOT_FOUND);

        List<EndPointRes> list = new ArrayList<>();
        Optional<EndPointRes> endPointResOpt = resRepository.findByEndPointAndStatus(endPointOpt.get(), 1);
        if (endPointResOpt.isPresent()) {
            EndPointRes endPointRes = endPointResOpt.get();
            endPointRes.setStatus(0);
            list.add(endPointRes);
        }
        Optional<EndPointRes> byId = resRepository.findById(request.getResId());
        if (byId.isPresent()) {
            EndPointRes endPointRes = byId.get();
            endPointRes.setStatus(1);
            list.add(endPointRes);
        }

        resRepository.saveAll(list);
        return new ResponseEntity<>("Response updated", HttpStatus.OK);
    }

    public ResponseEntity<?> getEndPoint() {
        return new ResponseEntity<>(endPointRepository.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<?> getEndPoint(Long id) {
        Optional<EndPoint> endPointOpt = endPointRepository.findById(id);
        if (endPointOpt.isEmpty())
            return new ResponseEntity<>("Invalid Endpoint ID", HttpStatus.NOT_FOUND);

        EndPoint endPoint = endPointOpt.get();
        EndPointDto endPointDto = new EndPointDto();
        endPointDto.setTitle(endPoint.getTitle());
        endPointDto.setPath(endPointDto.getPath());
        endPointDto.setMethod(endPointDto.getMethod());

        List<EndPointRes> resList = resRepository.findAllByEndPoint(endPoint);
        endPointDto.setResponseList(resList.stream().map(ResponseDto::new).toList());

        return new ResponseEntity<>(endPointDto, HttpStatus.OK);
    }

    public ResponseEntity<?> storeNewEndPoint(EndPointDto request) {
        Optional<EndPoint> endPointOpt = endPointRepository.findByPathAndMethod(request.getPath(), request.getMethod());
        if (endPointOpt.isPresent())
            return new ResponseEntity<>("EndPoint already exist : " + request.getMethod() + " " + request.getPath(), HttpStatus.BAD_REQUEST);

        EndPoint endPoint = EndPoint.builder()
                .title(request.getTitle())
                .path(request.getPath())
                .method(request.getMethod())
                .build();
        endPoint = endPointRepository.save(endPoint);

        if (!request.getResponseList().isEmpty()) {
            List<EndPointRes> list = new ArrayList<>();
            for (ResponseDto req : request.getResponseList()) {
                EndPointRes res = EndPointRes.builder()
                        .endPoint(endPoint)
                        .res(req.getRes())
                        .statusCode(req.getStatusCode())
                        .status(0)
                        .build();
                list.add(res);
            }
            resRepository.saveAll(list);
        }
        init();
        return new ResponseEntity<>("Endpoint added", HttpStatus.OK);
    }

    public ResponseEntity<?> storeNewResponse(ResponseDto request) {
        Optional<EndPoint> endPointOpt = endPointRepository.findById(request.getEndPointId());
        if (endPointOpt.isEmpty())
            return new ResponseEntity<>("Invalid Endpoint", HttpStatus.BAD_REQUEST);

        EndPointRes res = EndPointRes.builder()
                .endPoint(endPointOpt.get())
                .res(request.getRes())
                .statusCode(request.getStatusCode())
                .status(0)
                .build();
        resRepository.save(res);
        return new ResponseEntity<>("Response added", HttpStatus.OK);
    }

    public ResponseEntity<?> removeRes(ResDeleteRequest request) {
        resRepository.delete(new EndPointRes(request.getResId()));
        return new ResponseEntity<>("Response removed", HttpStatus.OK);
    }
}
