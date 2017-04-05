package com.femon.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.femon.entity.Server;

/**
 * Created by boshu2 on 2016/2/25.
 */
public interface ServerDao extends PagingAndSortingRepository<Server, Integer> {
}
