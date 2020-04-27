package com.spring.restaurant.service.mapper;

public interface Mapper<D, E> {
    D mapEntityToDomain(E entity);
    E mapDomainToEntity(D domain);
}