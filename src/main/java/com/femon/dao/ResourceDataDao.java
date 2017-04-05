package com.femon.dao;

import com.femon.entity.ResourceData;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by boshu2 on 2016/2/24.
 */
public interface ResourceDataDao extends PagingAndSortingRepository<ResourceData, Integer> {
    @Query("select rd from ResourceData rd where rd.serverId = ?1")
    List<ResourceData> findByResourceId(int serverId);

    /**   
     * 分页查询ResourceData   
    
     * @author 一剑  2016年2月25日 上午10:57:50   
     * @param serverId
     * @param pageable
     * @return  
     * @since JDK 1.7  
     */
    @Query("select rd from ResourceData rd where rd.serverId = ?1")
    List<ResourceData> findByResourceId(int serverId, Pageable pageable);
}
