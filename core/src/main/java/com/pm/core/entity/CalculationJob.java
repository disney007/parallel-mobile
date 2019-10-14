package com.pm.core.entity;

import com.pm.core.model.calculation.CalJobState;
import lombok.*;
import org.checkerframework.checker.units.qual.C;
import org.hibernate.annotations.Fetch;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    CalJobState state;

    @Column(name = "result")
    String result;

    @Column(name = "result_timestamp")
    Long resultTimestamp;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<CalculationJobExecution> executions;

    public String getInfo() {
        return "id = " + id + "," +
                "requestId = " + requestId + "," +
                "state = " + state;
    }

    public List<CalculationJobExecution> getExecutions() {
        if (this.executions == null) {
            this.executions = new ArrayList<>();
        }
        return this.executions;
    }

    public Optional<CalculationJobExecution> getExecution(UUID executionId) {
        return this.getExecutions().stream().filter(exec -> exec.getId().equals(executionId)).findFirst();
    }
}
