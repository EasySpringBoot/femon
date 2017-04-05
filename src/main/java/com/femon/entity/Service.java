/**
 * wai interface.java com.wai.dao
 */
package com.femon.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author 一剑 2015年12月23日 下午4:10:18
 */
@Entity
@Table(name = "fm_service")
public class Service {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	private String name;
	private String host;
	private int hostCode;
	private String requestUrl;
	private String responseBody;
	private String expect;
	private String method;
	private int paramsType;// 0 : 无参数 1 ： 普通参数 2 ： json参数
	private String paramsMap;

	private int todaySuccessTimes;
	private int todayFailTimes;
	/**
	 * 0 失败 1 正常
	 */
	private int state;

	private int totalSuccessTimes;
	private int totalFailTimes;
	private Date gmtCreate;
	private Date gmtModify;

	public String getParamsMap() {
		return paramsMap;
	}

	public void setParamsMap(String paramsMap) {
		this.paramsMap = paramsMap;
	}

	public Date getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public Date getGmtModify() {
		return gmtModify;
	}

	public void setGmtModify(Date gmtModify) {
		this.gmtModify = gmtModify;
	}

	public int getTodaySuccessTimes() {
		return todaySuccessTimes;
	}

	public void setTodaySuccessTimes(int todaySuccessTimes) {
		this.todaySuccessTimes = todaySuccessTimes;
	}

	public int getTodayFailTimes() {
		return todayFailTimes;
	}

	public void setTodayFailTimes(int todayFailTimes) {
		this.todayFailTimes = todayFailTimes;
	}

	public int getTotalSuccessTimes() {
		return totalSuccessTimes;
	}

	public void setTotalSuccessTimes(int totalSuccessTimes) {
		this.totalSuccessTimes = totalSuccessTimes;
	}

	public int getTotalFailTimes() {
		return totalFailTimes;
	}

	public void setTotalFailTimes(int totalFailTimes) {
		this.totalFailTimes = totalFailTimes;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getHostCode(){
		return hostCode;
	}

	public void setHostCode(int hostCode){
		this.hostCode = hostCode;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getRequestUrl() {
		return requestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	public String getResponseBody() {
		return responseBody;
	}

	public void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getExpect() {
		return expect;
	}

	public void setExpect(String expect) {
		this.expect = expect;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public int getParamsType() {
		return paramsType;
	}

	public void setParamsType(int paramsType) {
		this.paramsType = paramsType;
	}

	@Override
	public String toString() {
		return "Service [id=" + id + ", name=" + name + ", host=" + host + ", hostCode=" + hostCode + ", requestUrl=" + requestUrl
				+ ", responseBody=" + responseBody + ", expect=" + expect + ", method=" + method + ", paramsType="
				+ paramsType + ", paramsMap=" + paramsMap + ", todaySuccessTimes=" + todaySuccessTimes
				+ ", todayFailTimes=" + todayFailTimes + ", state=" + state + ", totalSuccessTimes=" + totalSuccessTimes
				+ ", totalFailTimes=" + totalFailTimes + ", gmtCreate=" + gmtCreate + ", gmtModify=" + gmtModify + "]";
	}

}
