package com.nk.mock.dynamic.endpoint;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EndPointRes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "endpoint_id", referencedColumnName = "id")
    private EndPoint endPoint;
    private int statusCode;
    private String res;
    private int status;

    public EndPointRes(Long resId) {
        id = resId;
    }
}
