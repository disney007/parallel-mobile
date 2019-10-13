package com.pm.core.entity;

import com.pm.core.model.calculation.CalJobExecState;
import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Data
@ToString
@Entity
@Table(name = "calculation_job_execution", schema = "public")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalculationJobExecution {
    @Id
    @Column(name = "id")
    UUID id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "job_id")
    CalculationJob job;

    @Column(name = "exec_device_id")
    String execDeviceId;

    @Column(name = "exec_username")
    String execUsername;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    CalJobExecState state;

    @Column(name = "created_timestamp")
    Long createdTimestamp;

    @Column(name = "update_timestamp")
    Long updateTimestamp;
}
