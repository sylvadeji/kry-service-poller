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
@Entity(name = "SERVICES")
public class ServiceModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SERVICE_ID")
    private Long serviceId;
    @Column(name = "SERVICE_URL")
    private String service_Url;
    @Column(name = "NAME")
    private String name;
    @Column(name = "DATE_CREATED")
    private ZonedDateTime dateCreated;
    @Column(name = "LAST_DATE_MODIFIED")
    private ZonedDateTime lastDateModified;
    @ManyToOne(targetEntity = UserModel.class, fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")
    private UserModel user;
    @Column(name = "STATUS")
    private int status;
}
