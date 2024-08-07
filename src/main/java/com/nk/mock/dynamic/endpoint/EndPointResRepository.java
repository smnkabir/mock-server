package com.nk.mock.dynamic.endpoint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EndPointResRepository extends JpaRepository<EndPointRes, Long> {
    Optional<EndPointRes> findByEndPointAndStatus(EndPoint endPoint, int status);
}
