package com.pm.core.entity;

import lombok.*;
import org.checkerframework.checker.units.qual.C;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Data
@ToString
@Entity
@Table(name = "calculation_job", schema = "public")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalculationJob {
    @Id
    @Column(name = "id")
    UUID id;

    @Column(name = "request_id")
    String requestId;

    @Column(name = "script")
    String script;

    @Column(name = "owner")
    String owner;

    @Column(name = "created_timestamp")
    Long createdTimestamp;

    @Column(name = "result")
    String result;

    @Column(name = "result_timestamp")
    Long resultTimestamp;
}
