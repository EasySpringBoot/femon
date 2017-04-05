package com.femon.controller;

import com.alibaba.fastjson.JSON;

import com.femon.dao.ServiceDao;
import com.femon.dao.ServiceDataDao;
import com.femon.entity.Service;
import com.femon.result.ServiceDataStatisticResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/data")
public class ServiceDataStatisticController {
    @Autowired
    ServiceDataDao serviceDataDao;
    @Autowired
    ServiceDao serviceDao;

    @RequestMapping(value = "/daysum", method = RequestMethod.GET)
    @ResponseBody
    public String dataDaySum(@RequestParam(value = "serviceId") int serviceId, @RequestParam(value = "date") String date
                             // yyyy-MM-dd
    ) {
        Service service = serviceDao.findOne(serviceId);

        int todaySuccessTimes = service.getTodaySuccessTimes();
        int todayFailTimes = service.getTotalFailTimes();
        int todayTotalTimes = todaySuccessTimes + todayFailTimes;
        ServiceDataStatisticResult ResultDataStatistic = new ServiceDataStatisticResult();
        ResultDataStatistic.setServiceId(serviceId);
        ResultDataStatistic.setDay(date);
        ResultDataStatistic.setFailTimes(todayFailTimes);
        ResultDataStatistic.setSuccessTimes(todaySuccessTimes);
        ResultDataStatistic.setTotalTimes(todayTotalTimes);

        return JSON.toJSONString(ResultDataStatistic);

    }

    @RequestMapping(value = "/totalsum", method = RequestMethod.GET)
    @ResponseBody
    public String dataTotalSum(@RequestParam(value = "serviceId") int serviceId) {

        Service service = serviceDao.findOne(serviceId);
        int failTimes = service.getTotalFailTimes();
        int successTimes = service.getTotalSuccessTimes();
        ServiceDataStatisticResult ResultDataStatistic = new ServiceDataStatisticResult();
        ResultDataStatistic.setServiceId(serviceId);
        ResultDataStatistic.setDay("");
        ResultDataStatistic.setFailTimes(failTimes);
        ResultDataStatistic.setSuccessTimes(successTimes);
        int totalTimes = failTimes + successTimes;
        ResultDataStatistic.setTotalTimes(totalTimes);

        return JSON.toJSONString(ResultDataStatistic);

    }

}
