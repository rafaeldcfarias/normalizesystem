package com.github.rafaeldcfarias.normalizesystem.dao;

import java.util.Set;

public interface OwnRepository<T> {
    Set<T> findAllDistinct();
}
