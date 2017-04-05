/**
 * wai InterfaceController.java com.wai.controller
 */
package com.femon.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.alibaba.fastjson.JSON;

import com.femon.dao.ServiceDao;
import com.femon.dao.ServiceDataDao;
import com.femon.entity.Service;
import com.femon.entity.ServiceData;
import com.femon.result.ServiceDataResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author 一剑 2015年12月29日 下午5:23:22
 */
@Controller
public class ServiceController {
    @Autowired
    ServiceDataDao historyDao;
    @Autowired
    ServiceDao serviceDao;

    @RequestMapping(value = "/")
    public ModelAndView index(Model model) {
        ModelAndView modelAndView = new ModelAndView("/index");
        return modelAndView;
    }

    @RequestMapping(value = "/index")
    public ModelAndView index2(Model model) {
        ModelAndView modelAndView = new ModelAndView("/index");
        return modelAndView;
    }

    @RequestMapping(value = "/service", method = RequestMethod.GET)
    public ModelAndView list(Model model, @RequestParam(value = "page", defaultValue = "0", required = false) int page,
                             @RequestParam(value = "count", defaultValue = "10", required = false) int count,
                             @RequestParam(value = "order", defaultValue = "ASC", required = false)
                                 Sort.Direction direction,
                             @RequestParam(value = "sort", defaultValue = "gmtCreate", required = false)
                                 String sortProperty,
                             @RequestParam(value = "hostCode", defaultValue = "1", required = false) int hostCode) {
        ModelAndView modelAndView = new ModelAndView("/service");

        Page result = null;

        if (hostCode == 0) {
            result = serviceDao.findAll(new PageRequest(page, count, new Sort(direction, sortProperty)));
        } else {
            // 根據host查詢
            result = serviceDao.findByHost(hostCode, new PageRequest(page, count, new Sort(direction, sortProperty)));
        }
        long totalPages = result.getTotalPages();
        // System.out.println("totalPages=" + totalPages);

        List<Integer> pageIndexList = new ArrayList<Integer>((int)totalPages);
        for (int i = 0; i < totalPages; i++) {
            pageIndexList.add(i);
        }
        int currentPage = page;
        List<Service> serviceList = result.getContent();
        // System.out.println("[serviceList]=" + serviceList);
        model.addAttribute("serviceList", serviceList);
        model.addAttribute("pageIndexList", pageIndexList);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("currentHost", hostCode);

        return modelAndView;
    }

    ///////////////////////////////////////////////////////////////////////////

    @RequestMapping(value = "/service/list", method = RequestMethod.GET)
    @ResponseBody
    public List<Service> list() {
        return serviceDao.findAll();
    }

    @RequestMapping(value = "/service/create", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView createService(Model model) {
        return new ModelAndView("/serviceCreate");
    }

    @RequestMapping(value = "/service/editPage", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView serviceEdit(Model model, @RequestParam(value = "serviceId") int serviceId) {
        Service service = serviceDao.findOne(serviceId);
        model.addAttribute("service", service);
        return new ModelAndView("/serviceEdit");
    }

    @RequestMapping(value = "/service/data", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView serviceData(Model model, @RequestParam(value = "serviceId") int serviceId) {
        Service service = serviceDao.findOne(serviceId);
        model.addAttribute("service", service);
        return new ModelAndView("/serviceStatistic");
    }

    @RequestMapping(value = "/service/detail", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView serviceDetail(Model model, @RequestParam(value = "serviceId") int serviceId) {
        Service service = serviceDao.findOne(serviceId);
        model.addAttribute("service", service);
        return new ModelAndView("/serviceDetail");
    }

    /**
     * 失败详情
     *
     * @param model
     * @param page
     * @param count
     * @param direction
     * @param sortProperty
     * @param serviceId
     * @return
     * @author 一剑 2016年2月22日 下午3:38:35
     * @since JDK 1.7
     */
    @RequestMapping(value = "/service/dataDetail", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView serviceDataDetail(Model model,
                                          @RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                          @RequestParam(value = "count", defaultValue = "10", required = false)
                                              int count,
                                          @RequestParam(value = "order", defaultValue = "ASC", required = false)
                                              Sort.Direction direction,
                                          @RequestParam(value = "sort", defaultValue = "gmtCreate", required = false)
                                              String sortProperty,
                                          @RequestParam(value = "serviceId") int serviceId) {
        Service service = serviceDao.findOne(serviceId);
        model.addAttribute("service", service);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String now = sdf.format(new Date());

        Date nowDate = new Date();
        try {
            nowDate = sdf.parse(now);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(nowDate);
        calendar.add(Calendar.DATE, 1);
        Date tomorrowNow = calendar.getTime();

        Page<ServiceData> serviceFailList = historyDao.findFailByServiceIdAndDay(serviceId, nowDate, tomorrowNow,
            new PageRequest(page, count, new Sort(direction, sortProperty)));
        long totalPages = serviceFailList.getTotalPages();
        System.out.println("totalPages=" + totalPages);

        List<Integer> pageIndexList = new ArrayList<Integer>((int)totalPages);
        for (int i = 0; i < totalPages; i++) {
            pageIndexList.add(i);
        }

        model.addAttribute("serviceFailList", serviceFailList.getContent());
        model.addAttribute("pageIndexList", pageIndexList);
        model.addAttribute("currentPage", page);

        return new ModelAndView("/serviceDataDetail");
    }

    @RequestMapping(value = "/service/new", method = RequestMethod.POST)
    @ResponseBody
    public Service newOne(@RequestParam(value = "name") String name, @RequestParam(value = "host") String host,
                          @RequestParam(value = "hostCode") int hostCode,
                          @RequestParam(value = "requestUrl") String requestUrl,
                          @RequestParam(value = "expect") String expect, @RequestParam(value = "method") String method,
                          @RequestParam(value = "paramsType") int paramsType,
                          @RequestParam(value = "paramsMap") String paramsMap) {

        Service service = new Service();

        service.setName(name);
        service.setHost(host);
        service.setHostCode(hostCode);
        service.setExpect(expect);
        service.setRequestUrl(requestUrl);
        service.setMethod(method);
        service.setState(0); // 默认失败状态
        service.setTodayFailTimes(0);
        service.setTodaySuccessTimes(0);
        service.setTotalSuccessTimes(0);
        service.setTotalFailTimes(0);
        service.setParamsType(paramsType);
        service.setParamsMap(paramsMap);

        service.setGmtCreate(new Date());
        service.setGmtModify(new Date());

        return serviceDao.save(service);

    }

    @RequestMapping(value = "/service/edit", method = RequestMethod.POST)
    @ResponseBody
    public Service editOne(@RequestParam(value = "id") int id, @RequestParam(value = "name") String name,
                           @RequestParam(value = "hostCode") int hostCode,
                           @RequestParam(value = "requestUrl") String requestUrl,
                           @RequestParam(value = "expect") String expect, @RequestParam(value = "method") String method,
                           @RequestParam(value = "paramsType") int paramsType,
                           @RequestParam(value = "paramsMap") String paramsMap) {

        Service service = serviceDao.findOne(id);

        service.setName(name);
        service.setHostCode(hostCode);
        service.setExpect(expect);
        service.setRequestUrl(requestUrl);
        service.setMethod(method);

        service.setParamsType(paramsType);
        service.setParamsMap(paramsMap);

        service.setGmtModify(new Date());

        return serviceDao.save(service);

    }

    @RequestMapping(value = "/service/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Service getOne(@PathVariable String id) {
        return serviceDao.findOne(Integer.valueOf(id));
    }

    @RequestMapping(value = "/serviceHistoryChart/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView mvchart(Model model, @PathVariable(value = "id") int id) {

        Service service = serviceDao.findOne(id);
        String serviceName = service.getName();

        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        model.addAttribute("currentDate", currentDate);
        model.addAttribute("serviceId", id);
        model.addAttribute("serviceName", serviceName);
        return new ModelAndView("/serviceDataChart");
    }

    @RequestMapping(value = "/service/{serviceId}/historyList", method = RequestMethod.GET)
    @ResponseBody
    public List<ServiceDataResult>  historylist(
        @PageableDefault(page = 0, value = 20, sort = {
            "sampleTime"}, direction = Sort.Direction.DESC) Pageable pageable,
        @PathVariable(value = "serviceId") int serviceId) {

        List<ServiceData> pageList = historyDao.findByServiceId(serviceId, pageable);
        Collections.reverse(pageList);
        List<ServiceDataResult> resultHistories = new ArrayList<ServiceDataResult>();

        for (ServiceData h : pageList) {
            Date sampleTime = h.getSampleTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String sampleTimeStr = sdf.format(sampleTime);
            int state = h.getState();
            String responseBody = h.getResponseBody();
            ServiceDataResult resultHistory = new ServiceDataResult();
            resultHistory.setServiceId(serviceId);
            resultHistory.setState(state);
            resultHistory.setSampleTime(sampleTimeStr);
            resultHistory.setResponseBody(responseBody);

            resultHistories.add(resultHistory);
        }

        return resultHistories;

    }

}
