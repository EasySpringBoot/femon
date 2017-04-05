package com.femon.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.femon.entity.Server;

/**
 * created by boshu, 2016/02/23
 */
public interface ResourceDao extends PagingAndSortingRepository<Server, Integer> {

}