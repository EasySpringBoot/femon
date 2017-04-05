package com.femon.dao;

import com.femon.entity.ProcessData;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by boshu2 on 2016/2/27.
 */
public interface ProcessDataDao extends PagingAndSortingRepository<ProcessData, Integer> {
    @Query(value = "select * from fm_process_data where process_id = ?1 order by sample_time desc limit 1", nativeQuery = true)
    ProcessData findOneByProcessId(int processId);

    @Query(value = "select * from fm_process_data where process_id = ?1 order by sample_time desc limit 40", nativeQuery = true)
    List<ProcessData> findByProcessId(int processId);

    @Query(value = "select count(*) from fm_process_data where process_id = ?1 and sample_time like ?2% and state = 1 order by sample_time desc", nativeQuery = true)
    int countOfTodaySuccess(int processId, String curDate);

    @Query(value = "select count(*) from fm_process_data where process_id = ?1 and sample_time like ?2% and state = 0 order by sample_time desc", nativeQuery = true)
    int countOfTodayFailure(int processId, String curDate);

    /**
     * 
     * 进程状态数据查询接口
     * 
     * @author 一剑 2016年3月1日 下午3:21:24
     * @param processId
     * @param state：0
     *            失败 1 正常
     * @param days：
     *            从当天到前n天（n=days）
     * @return
     * @since JDK 1.7
     */
    @Query(value = "select count(*) FROM femon.fm_process_data as pd where process_id=?1 and state=?2 and datediff(now(),pd.gmt_create )<=?3 order by sample_time desc", nativeQuery = true)
    int countStateOfDays(int processId, int state, int days);

    @Query(value = "select * FROM femon.fm_process_data as pd where process_id=?1 and state=0 and datediff(now(),pd.gmt_create )<=?3 order by sample_time desc", nativeQuery = true)
    List<ProcessData> findFailsByProcessIdOfDays(int processId, int days);
}
