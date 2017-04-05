package com.femon.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

import com.femon.dao.ProcessDao;
import com.femon.dao.ProcessDataDao;
import com.femon.dao.ResourceDao;
import com.femon.dao.ServerDao;
import com.femon.entity.Process;
import com.femon.entity.ProcessData;
import com.femon.entity.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by boshu2 on 2016/2/25.
 */
@Controller
public class ProcessController {
    @Autowired
    ProcessDao processDao;

    @Autowired
    ProcessDataDao processDataDao;

    @Autowired
    ResourceDao resourceDao;

    @Autowired
    ServerDao serverDao;

    @RequestMapping(value = "/process")
    public ModelAndView process(Model model) {
        ModelAndView modelAndView = new ModelAndView("/process");

        Iterator iterator = processDao.findAll().iterator();
        List<JSONObject> nodes = new ArrayList<JSONObject>();
        for (; iterator.hasNext(); ) {
            JSONObject node = new JSONObject();
            Process process = (Process)iterator.next();
            int serverId = process.getServerId();
            Server server = resourceDao.findOne(serverId);
            node.put("id", process.getId());
            node.put("name", process.getName());
            node.put("state", process.getState());

            node.put("serverName", server.getName());
            node.put("serverIp", server.getIp());

            nodes.add(node);
        }
        model.addAttribute("processList", nodes);
        return modelAndView;
    }

    @RequestMapping(value = "/process/create")
    public ModelAndView processCreate(Model model) {
        ModelAndView modelAndView = new ModelAndView("/processCreate");
        return modelAndView;
    }

    @RequestMapping(value = "/process/new", method = RequestMethod.POST)
    @ResponseBody
    public Process processNew(@RequestParam(value = "serverId") int serverId,
                              @RequestParam(value = "name") String name) {
        Process process = new Process();
        process.setServerId(serverId);
        process.setName(name);
        process.setState(0);
        process.setGmtCreate(new Date());
        return processDao.save(process);
    }

    @RequestMapping(value = "/process/edit/{processId}")
    public ModelAndView gotoEditPage(Model model, @PathVariable(value = "processId") int processId) {

        Process process = processDao.findOne(processId);
        model.addAttribute("process", process);

        int serverId = process.getServerId();
        Server s = serverDao.findOne(serverId);
        String serverName = s.getName();
        model.addAttribute("serverId", serverId);
        model.addAttribute("serverName", serverName);
        return new ModelAndView("/processEdit");

    }

    @RequestMapping(value = "/process/editPost", method = RequestMethod.POST)
    @ResponseBody
    public Process processEdit(@RequestParam(value = "processId") int processId,
                               @RequestParam(value = "serverId") int serverId,
                               @RequestParam(value = "name") String name) {
        Process process = processDao.findOne(processId);
        if (process == null) { return null; }

        process.setServerId(serverId);
        process.setName(name);
        process.setGmtCreate(new Date());
        Process result = processDao.save(process);
        return result;
    }

    @RequestMapping(value = "/process/serverList")
    @ResponseBody
    public List<Server> processServerList() {
        Iterable iterable = resourceDao.findAll();
        Iterator itor = iterable.iterator();
        List<Server> servers = new ArrayList<Server>();
        for (; itor.hasNext(); ) { servers.add((Server)itor.next()); }
        return servers;
    }

    @RequestMapping(value = "/process/detail/{processId}")
    public ModelAndView processDetail(Model model, @PathVariable(value = "processId") int processId) {
        ModelAndView modelAndView = new ModelAndView("/processDetail");
        Process process = processDao.findOne(processId);
        JSONObject node = new JSONObject();
        int serverId = process.getServerId();
        Server server = serverDao.findOne(serverId);
        node.put("id", process.getId());
        node.put("name", process.getName());
        node.put("serverName", server.getName());
        model.addAttribute("process", node);
        return modelAndView;
    }

    @RequestMapping(value = "/process/output/{processId}")
    @ResponseBody
    public String processOutput(@PathVariable(value = "processId") int processId) {
        ProcessData processDatas = processDataDao.findOneByProcessId(processId); // 取最新一条记录
        if (processDatas != null) { return processDatas.getOutput(); }
        return null;
    }

    @RequestMapping(value = "/process/monitor/{processId}")
    @ResponseBody
    public List<JSONObject> processMonitor(@PathVariable(value = "processId") int processId) {
        List<ProcessData> processDatas = processDataDao.findByProcessId(processId);
        Collections.reverse(processDatas);
        int dataSize = processDatas.size();
        if (dataSize > 0) {
            List<JSONObject> nodes = new ArrayList<JSONObject>();
            for (int i = 0; i < dataSize; i++) {
                JSONObject node = new JSONObject();
                Date sampleTime = processDatas.get(i).getSampleTime();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                String sampleTimeStr = sdf.format(sampleTime);

                node.put("sampleTime", sampleTimeStr);
                node.put("state", processDatas.get(i).getState());
                node.put("processId", processDatas.get(i).getProcessId());
                nodes.add(node);
            }
            return nodes;
        } else {
            return null;
        }
    }

    @RequestMapping(value = "/processDataChart/{processId}")
    public ModelAndView processMonitorChart(@PathVariable(value = "processId") int processId, Model model) {
        Process process = processDao.findOne(processId);
        String processName = process.getName();
        int serverId = process.getServerId();
        Server server = serverDao.findOne(serverId);
        String serverName = server.getName();
        String ip = server.getIp();

        model.addAttribute("processName", processName);
        model.addAttribute("serverName", serverName);
        model.addAttribute("ip", ip);

        return new ModelAndView("/processDataChart");
    }

    @RequestMapping(value = "/process/{processId}/today")
    @ResponseBody
    public String processSuccessToday(@PathVariable(value = "processId") int processId) {
        Date curDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String sampleTimeStr = sdf.format(curDate);
        int success = processDataDao.countOfTodaySuccess(processId, sampleTimeStr);
        int failure = processDataDao.countOfTodayFailure(processId, sampleTimeStr);
        JSONObject json = new JSONObject();
        json.put("todaySuccess", success);
        json.put("todayFailure", failure);
        return json.toJSONString();
    }

    @RequestMapping(value = "/processState/{processId}")
    @ResponseBody
    public String processState(@PathVariable(value = "processId") int processId,
                               @RequestParam(value = "state") int state, @RequestParam(value = "days") int days) {
        int count = processDataDao.countStateOfDays(processId, state, days);
        JSONObject json = new JSONObject();
        json.put("count", count);
        return json.toJSONString();
    }

    @RequestMapping(value = "/process/{processId}/statistic", method = RequestMethod.GET)
    public ModelAndView processStatistic(Model model, @PathVariable(value = "processId") int processId) {
        System.out.println("processId=" + processId);
        Process process = processDao.findOne(processId);
        model.addAttribute("process", process);

        int todaySuccess = processDataDao.countStateOfDays(processId, 1, 0);
        int todayFail = processDataDao.countStateOfDays(processId, 0, 0);

        int sevenDaySuccess = processDataDao.countStateOfDays(processId, 1, 7);
        int sevenDayFail = processDataDao.countStateOfDays(processId, 0, 7);

        int monthSuccess = processDataDao.countStateOfDays(processId, 1, 30);
        int monthFail = processDataDao.countStateOfDays(processId, 0, 30);

        model.addAttribute("todaySuccess", todaySuccess);
        model.addAttribute("todayFail", todayFail);
        model.addAttribute("todayMtbf", todayFail / (todaySuccess + todayFail));

        model.addAttribute("weekSuccess", sevenDaySuccess);
        model.addAttribute("weekFail", sevenDayFail);
        model.addAttribute("weekMtbf", sevenDayFail / (sevenDayFail + sevenDaySuccess));

        model.addAttribute("monthSuccess", monthSuccess);
        model.addAttribute("monthFail", monthFail);
        model.addAttribute("monthMtbf", monthFail / (monthSuccess + monthFail));

        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        model.addAttribute("today", today);
        return new ModelAndView("/processStatistic");
    }

}
