package com.pm.core.entity;

import com.pm.core.model.State;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "account", schema = "public")
public class Account {
    @Id
    @Column(name = "username")
    String username;

    @Column(name = "name")
    String name;

    @Column(name = "created_timestamp")
    Long createdTimestamp;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    State state;
}
