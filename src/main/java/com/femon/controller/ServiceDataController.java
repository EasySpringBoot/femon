/**
 * wai ProjectController.java com.wai.controller
 */
package com.femon.controller;

import com.femon.entity.ServiceData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.femon.dao.ServiceDataDao;
import com.femon.dao.ServiceDao;

/**
 * @author 一剑 2015年12月23日 下午4:28:25
 */
@Controller
@RequestMapping("/history")
public class ServiceDataController {
	@Autowired
	ServiceDataDao serviceDataDao;
	@Autowired
	ServiceDao serviceDao;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ServiceData getOne(@PathVariable String id) {
		return serviceDataDao.findOne(Integer.valueOf(id));
	}

}
