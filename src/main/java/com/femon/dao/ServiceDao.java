/**
 * wai InterfaceDao.java com.wai.dao
 */
package com.femon.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.femon.entity.Service;

/**
 * @author 一剑 2015年12月29日 下午4:28:22
 */
public interface ServiceDao extends PagingAndSortingRepository<Service, Integer> {

	@Query("select s from Service s where s.hostCode = ?1")
	Page<Service> findByHost(int hostCode, Pageable pageable);

	List<Service> findAll();

}
