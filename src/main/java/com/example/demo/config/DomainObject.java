package com.example.demo.config;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@MappedSuperclass
public class DomainObject implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id = UUID.randomUUID();

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column(name = "created", nullable = false, updatable = false)
    private Date created = new Date();

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column(name = "modified", nullable = false, updatable = false)
    private Date modified = new Date();

    @Column(name = "active", nullable = false, updatable = false)
    private Boolean active = true;

    @Column(name = "deleted", nullable = false, updatable = false)
    private Boolean deleted = false;

}
