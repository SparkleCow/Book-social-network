package com.sparklecow.book.services;

public interface CrudService <C, R, U>{
    R create(C c);
    Iterable<R> findAll();
    R findById(Integer id);
    void updateById(U u, Integer id);
    void deleteById(Integer id);
}
