package com.pm.core.repository;

import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

public class BaseRepository {
    @PersistenceContext
    EntityManager entityManager;

    protected Session session() {
        return entityManager.unwrap(Session.class);
    }

    <T> Optional<T> getFirst(List<T> list) {
        if (list != null && list.size() > 0) {
            return Optional.of(list.get(0));
        }

        return Optional.empty();
    }
}
