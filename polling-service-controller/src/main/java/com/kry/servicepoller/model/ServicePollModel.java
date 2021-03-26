package com.kry.servicepoller.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "SERVICE_POOL")
public class ServicePollModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POOL_ID")
    private Long poolId;
    @ManyToOne(targetEntity = ServiceModel.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "SERVICE_ID", referencedColumnName = "SERVICE_ID")
    private ServiceModel service;
    @Column(name = "POOL_RESULT")
    private String result;
    @Column(name = "POOL_TIME")
    private ZonedDateTime poolTime;
}
