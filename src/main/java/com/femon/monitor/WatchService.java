package com.femon.monitor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;

import com.femon.config.Constant;
import com.femon.dao.ServiceDao;
import com.femon.dao.ServiceDataDao;
import com.femon.engine.CookieUtil;
import com.femon.engine.HttpInvoker;
import com.femon.engine.HttpSimpleEngine;
import com.femon.entity.Service;
import com.femon.entity.ServiceData;
import com.femon.mail.FemonMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author 一剑 2016年1月8日 下午10:53:36
 */
@Component
public class WatchService {
    /**
     * Global Login session cookie
     */
    private final static String loginUserName = Constant.LOGIN_USERNAME_TEST;
    private final static String loginPassword = Constant.LOGIN_PASSWORD_TEST;
    @Autowired
    ServiceDataDao historyDao;
    @Autowired
    ServiceDao serviceDao;

    /**
     * 遍历监控的service Job
     */
    @Scheduled(fixedRate = 60000)
    public void curlJob() {
        System.out.println("=================================定时执行=============================");
        Iterable iterable = serviceDao.findAll();
        Iterator itor = iterable.iterator();
        List<Service> serviceList = new ArrayList<Service>();
        for (; itor.hasNext(); ) {
            serviceList.add((Service)itor.next());
        }
        if (serviceList.isEmpty()) {
            return;
        }

        // 如果是零點,在最开始的1min内，清空today_success_times, today_fail_times字段值
        if (isZeroHourAndZeroMinute()) {
            for (Service service : serviceList) {
                Integer id = service.getId();
                Service s = serviceDao.findOne(id);
                s.setTodayFailTimes(0);
                s.setTodaySuccessTimes(0);
                serviceDao.save(s);
            }
        } else {

            for (Service service : serviceList) {
                if ("POST".equalsIgnoreCase(service.getMethod())) {
                    processPostRequest(service);
                }
                if ("GET".equalsIgnoreCase(service.getMethod())) {
                    processGetRequest(service);
                }
            }

        }

    }

    private void processGetRequest(Service service) {

        String host = service.getHost();
        String requestUrl = service.getRequestUrl();
        String apiUrl = host + requestUrl;
        String serviceName = service.getName();
        //String loginUrl = host + Constant.LOGIN_URL_H5;
        //String cookie = CookieUtil.getLoginCookie(loginUserName, loginPassword, loginUrl);
        //String responseBody = HttpSimpleEngine.getWithCookieReturnBody(apiUrl, cookie);
        String responseBody = HttpSimpleEngine.getReturnBody(apiUrl);
        System.out.println(responseBody);
        Integer id = service.getId();
        Service s = serviceDao.findOne(id);

        String expect = service.getExpect();
        if (responseBody.contains(expect)) {
            s.setState(1); // 1 成功 0 失败
            // 成功次數+1
            int totalSuccessTimes = s.getTotalSuccessTimes() + 1;
            s.setTotalSuccessTimes(totalSuccessTimes);

            int todaySuccessTimes = s.getTodaySuccessTimes() + 1;
            s.setTodaySuccessTimes(todaySuccessTimes);

        } else {
            s.setState(0);

            int totalFailTimes = s.getTotalFailTimes() + 1;
            s.setTotalFailTimes(totalFailTimes);

            int todayFailTimes = s.getTodayFailTimes() + 1;
            s.setTodayFailTimes(todayFailTimes);

            String content = "<h1>测试环境<p style='color:red;font-weight:bold'>" + serviceName + "</p>服务失败，详情如下：</h1>"
                + "<p>" + "API： " + apiUrl + "Expect: " + expect + "<p>"
                + "Response: " + responseBody;

            // sendMail(content);
        }

        if (responseBody.length() > 10000) {
            responseBody = responseBody.substring(0, 10000);
        }

        s.setResponseBody(responseBody);
        serviceDao.save(s);

        recordHistory(s, responseBody);

    }

    private void processPostRequest(Service service) {
        String host = service.getHost();
        String requestUrl = service.getRequestUrl();
        String apiUrl = host + requestUrl;
        // System.out.println("请求地址: " + apiUrl);

        String serviceName = service.getName();
        String loginUrl = host + Constant.LOGIN_URL_H5;
        String cookie = CookieUtil.getLoginCookie(loginUserName, loginPassword, loginUrl);

        String params = service.getParamsMap();
        // System.out.println("参数内容: " + params);

        if (null == params || params.isEmpty()) {
            processPostWithoutParams(apiUrl, cookie, service);
        } else {
            Map paramsMap = JSON.parseObject(params, Map.class);
            processPostWithParams(apiUrl, cookie, service, paramsMap);
        }

    }

    private void processPostWithParams(String apiUrl, String cookie, Service service, Map paramsMap) {

        String responseBody = HttpInvoker.postWithParamsAndCookieReturnBody(apiUrl, paramsMap, cookie);

        if (responseBody.length() > 10000) {
            responseBody = responseBody.substring(0, 10000);
        }

        Integer id = service.getId();
        Service s = serviceDao.findOne(id);

        s.setResponseBody(responseBody);
        String expect = service.getExpect();
        if (responseBody.contains(expect)) {
            s.setState(1); // 1 成功 0 失败
            // 成功次數+1
            int totalSuccessTimes = s.getTotalSuccessTimes() + 1;
            s.setTotalSuccessTimes(totalSuccessTimes);

            int todaySuccessTimes = s.getTodaySuccessTimes() + 1;
            s.setTodaySuccessTimes(todaySuccessTimes);

        } else {
            s.setState(0);

            int totalFailTimes = s.getTotalFailTimes() + 1;
            s.setTotalFailTimes(totalFailTimes);

            int todayFailTimes = s.getTodayFailTimes() + 1;
            s.setTodayFailTimes(todayFailTimes);

            String content = "<h1>测试环境<p style='color:red;font-weight:bold'>" + s.getName() + "</p>服务失败，详情如下：</h1>"
                + "<p>" + "API： " + apiUrl + "<p>" + "cookie: " + cookie + "<p>" + "Expect: " + expect + "<p>"
                + "Response: " + responseBody;

            sendMail(content);
        }

        serviceDao.save(s);

        recordHistory(s, responseBody);
    }

    private void processPostWithoutParams(String apiUrl, String cookie, Service service) {
        String responseBody = HttpSimpleEngine.postWithCookieReturnBody(apiUrl, cookie);

        if (responseBody.length() > 10000) {
            responseBody = responseBody.substring(0, 10000);
        }

        Integer id = service.getId();
        Service s = serviceDao.findOne(id);

        s.setResponseBody(responseBody);
        String expect = service.getExpect();
        if (responseBody.contains(expect)) {
            s.setState(1); // 1 成功 0 失败
            // 成功次數+1
            int totalSuccessTimes = s.getTotalSuccessTimes() + 1;
            s.setTotalSuccessTimes(totalSuccessTimes);

            int todaySuccessTimes = s.getTodaySuccessTimes() + 1;
            s.setTodaySuccessTimes(todaySuccessTimes);

        } else {
            s.setState(0);

            int totalFailTimes = s.getTotalFailTimes() + 1;
            s.setTotalFailTimes(totalFailTimes);

            int todayFailTimes = s.getTodayFailTimes() + 1;
            s.setTodayFailTimes(todayFailTimes);

            String content = "<h1>测试环境<p style='color:red;font-weight:bold'>" + s.getName() + "</p>服务失败，详情如下：</h1>"
                + "<p>" + "API： " + apiUrl + "<p>" + "cookie: " + cookie + "<p>" + "Expect: " + expect + "<p>"
                + "Response: " + responseBody;

            sendMail(content);
        }

        serviceDao.save(s);

        recordHistory(s, responseBody);

    }

    private boolean isZeroHourAndZeroMinute() {
        Calendar c = Calendar.getInstance();
        int hh = c.get(Calendar.HOUR_OF_DAY);
        int mm = c.get(Calendar.MINUTE);
        int ss = c.get(Calendar.SECOND);

        if (hh == 0 && mm == 0) {
            // System.out.println("零点零分");// 系统在这一分钟内，清一下当天的数据
            return true;
        } else {
            return false;
        }
    }

    private void sendMail(String content) {
        String sendToMailList = "";
        for (String mail : Constant.MAIL_LIST) {
            sendToMailList += mail + ",";
        }
        sendToMailList = sendToMailList.substring(0, sendToMailList.lastIndexOf(","));
        String smtp = "smtp.163.com";
        String from = "***";
        String to = sendToMailList;
        String copyto = "***";
        String subject = "QA_ENV_MONITOR-测试环境监控报告";
        String username = "QA_ENV_MONITOR";
        String password = "****";
        String filename = "****";// 附件路径
        FemonMail.sendAndCc(smtp, from, to, copyto, subject, content, username, password, filename);

    }

    /**
     * 记录历史表
     */
    private void recordHistory(Service s, String responseBody) {
        if (responseBody.length() > 10000) {
            responseBody = responseBody.substring(0, 10000);
        }

        // 记录历史表
        ServiceData History = new ServiceData();
        History.setGmtCreate(new Date());
        History.setSampleTime(new Date());
        History.setServiceId(s.getId());
        History.setState(s.getState());
        History.setResponseBody(responseBody);
        History.setTodayTotalSuccessTimes(s.getTodaySuccessTimes());
        History.setTodayTotalFailTimes(s.getTodayFailTimes());

        History.setTotalSuccessTimes(s.getTotalSuccessTimes());
        History.setTotalFailTimes(s.getTotalFailTimes());

        historyDao.save(History);

    }

}
