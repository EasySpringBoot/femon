package com.femon.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.alibaba.fastjson.JSON;
import com.femon.dao.ResourceDao;
import com.femon.dao.ResourceDataDao;
import com.femon.entity.ResourceData;
import com.femon.entity.Server;
import com.femon.result.ResourceDataResult;

/**
 * created by boshu, 2016/02/23
 */
@Controller
public class ResourceController {
    @Autowired
    ResourceDao resourceDao;

    @Autowired
    ResourceDataDao resourceDataDao;

    @RequestMapping(value = "/resource", method = RequestMethod.GET)
    public ModelAndView resourceList(Model model) {
        ModelAndView modelAndView = new ModelAndView("/resource");

        Iterable iterable = resourceDao.findAll();
        Iterator itor = iterable.iterator();
        List<Server> serverList = new ArrayList<Server>();
        for (; itor.hasNext();) {
            serverList.add((Server) itor.next());
        }

        model.addAttribute("resourceList", serverList);

        return modelAndView;
    }

    @RequestMapping(value = "/resource/create")
    public ModelAndView resourceCreate(Model model) {
        return new ModelAndView("/resourceCreate");
    }

    @RequestMapping(value = "/resource/new", method = RequestMethod.POST)
    @ResponseBody
    public String resourceNew(@RequestParam(value = "name") String name, @RequestParam(value = "ip") String ip,
            @RequestParam(value = "username") String username, @RequestParam(value = "password") String password) {
        Server res = new Server();
        res.setName(name);
        res.setIp(ip);
        res.setUsername(username);
        res.setPassword(password);
        res.setGmtCreate(new Date());

        Server result = resourceDao.save(res);
        return JSON.toJSONString(result);
    }

    @RequestMapping(value = "/resource/detail")
    public ModelAndView resourceDetail(Model model, @RequestParam(value = "resourceId") int resourceId) {
        ModelAndView modelAndView = new ModelAndView("/resourceDetail");

        Server server = resourceDao.findOne(resourceId);
        model.addAttribute("resource", server);

        return modelAndView;
    }

    @RequestMapping(value = "/resource/info", method = RequestMethod.GET)
    @ResponseBody
    public String resouceInfo(@RequestParam(value = "resourceId") int resourceId) {
        List<ResourceData> resourceDatas = resourceDataDao.findByResourceId(resourceId);

        if (resourceDatas == null || resourceDatas.isEmpty()) {
            return null;
        }

        Map<String, String> map = new HashMap<String, String>();
        for (ResourceData resourceData : resourceDatas) {
            map.put(resourceData.getSampleTime().toString(), resourceData.getDiskAvail() + "");
        }

        return JSON.toJSONString(map);
    }

    @RequestMapping(value = "/resourceDataChart/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView mvchart(Model model, @PathVariable(value = "id") int id) {

        Server Server = resourceDao.findOne(id);
        String ResourceName = Server.getName();

        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        model.addAttribute("currentDate", currentDate);
        model.addAttribute("ResourceId", id);
        model.addAttribute("ResourceName", ResourceName);
        return new ModelAndView("/resourceDataChart");
    }

    @RequestMapping(value = "/resource/{resourceId}/datalist", method = RequestMethod.GET)
    @ResponseBody
    public String historyList(
            @PageableDefault(page = 0, value = 40, sort = {
                    "sampleTime" }, direction = Sort.Direction.DESC) Pageable pageable,
            @PathVariable(value = "resourceId") int resourceId) {

        List<ResourceData> pageList = resourceDataDao.findByResourceId(resourceId, pageable);
        Collections.reverse(pageList);
        List<ResourceDataResult> ResourceDataResults = new ArrayList<ResourceDataResult>();

        for (ResourceData rd : pageList) {
            Date sampleTime = rd.getSampleTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String sampleTimeStr = sdf.format(sampleTime);

            Double diskAvail = rd.getDiskAvail();
            Double cpuAvail = rd.getCpuAvail();
            Double memAvail = rd.getMemAvail();

            ResourceDataResult result = new ResourceDataResult();
            result.setResourceId(resourceId);
            result.setSampleTime(sampleTimeStr);
            result.setDiskAvail(diskAvail);
            result.setCpuUsage(cpuAvail);
            result.setMemFree(memAvail);

            ResourceDataResults.add(result);
        }

        return JSON.toJSONString(ResourceDataResults);

    }

    @RequestMapping(value = "/resource/edit", method = RequestMethod.GET)
    public ModelAndView resourceEdit(Model model, @RequestParam(value = "serverId") int serverId) {
        ModelAndView modelAndView = new ModelAndView("/resourceEdit");

        Server server = resourceDao.findOne(serverId);
        model.addAttribute("server", server);
        return modelAndView;
    }

    @RequestMapping(value = "/resource/update", method = RequestMethod.POST)
    @ResponseBody
    public String resourceUpdate(@RequestParam(value = "serverId") int serverId,
            @RequestParam(value = "name") String name, @RequestParam(value = "ip") String ip,
            @RequestParam(value = "username") String username, @RequestParam(value = "password") String password) {
        Server server = resourceDao.findOne(serverId);
        server.setName(name);
        server.setUsername(username);
        server.setPassword(password);
        server.setIp(ip);

        Server result = resourceDao.save(server);
        return JSON.toJSONString(result);
    }
}
