package com.femon.dao;

import java.util.Date;
import java.util.List;

import com.femon.entity.ServiceData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author 一剑 2015年12月23日 下午9:34:15
 */
public interface ServiceDataDao extends PagingAndSortingRepository<ServiceData, Integer> {

    @Query("select h from ServiceData h where h.serviceId = ?1")
    List<ServiceData> findByServiceId(int serviceId, Pageable pageable);

    @Query("select h from ServiceData h where h.serviceId = ?1 and h.sampleTime between ?2 and ?3 ")
    List<ServiceData> findByServiceIdAndDay(int serviceId, Date dayStart, Date dayEnd, Pageable pageable);

    @Query("select h from ServiceData h where h.serviceId = ?1 and state=0 and h.sampleTime between ?2 and ?3 ")
    Page<ServiceData> findFailByServiceIdAndDay(int serviceId, Date dayStart, Date dayEnd, Pageable pageable);

    ServiceData save(ServiceData h);

}
