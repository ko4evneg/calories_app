package com.github.ko4evneg.caloriesApp.repository.inmemory;

import com.github.ko4evneg.caloriesApp.model.AbstractBaseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class InMemoryBaseRepository<T extends AbstractBaseEntity> {
    protected static final Logger log = LoggerFactory.getLogger(InMemoryUserRepository.class);

    protected static final AtomicInteger idCounter = new AtomicInteger(1);
    protected final Map<Integer, T> repository;

    public InMemoryBaseRepository() {
        repository = new ConcurrentHashMap<>();
    }
}
