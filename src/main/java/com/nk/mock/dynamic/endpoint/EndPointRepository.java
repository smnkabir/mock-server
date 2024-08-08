package com.nk.mock.dynamic.endpoint;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EndPointRepository extends JpaRepository<EndPoint, Long> {
    Optional<EndPoint> findByPathAndMethod(String path, String method);
}
