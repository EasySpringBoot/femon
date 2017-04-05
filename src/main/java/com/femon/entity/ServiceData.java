/**
 * wai Case.java com.wai.dao
 */
package com.femon.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author 一剑 2015年12月23日 下午4:08:48
 */
@Entity
@Table(name = "fm_service_data")
public class ServiceData {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private Date gmtCreate;
	private Date sampleTime;
	private int serviceId;
	private int state;
	private int totalSuccessTimes;
	private int totalFailTimes;
	private int todayTotalSuccessTimes;
	private int todayTotalFailTimes;

	private String responseBody;

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

	public int getTodayTotalSuccessTimes() {
		return todayTotalSuccessTimes;
	}

	public void setTodayTotalSuccessTimes(int todayTotalSuccessTimes) {
		this.todayTotalSuccessTimes = todayTotalSuccessTimes;
	}

	public int getTodayTotalFailTimes() {
		return todayTotalFailTimes;
	}

	public void setTodayTotalFailTimes(int todayTotalFailTimes) {
		this.todayTotalFailTimes = todayTotalFailTimes;
	}

	public String getResponseBody() {
		return responseBody;
	}

	public void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public Date getSampleTime() {
		return sampleTime;
	}

	public void setSampleTime(Date sampleTime) {
		this.sampleTime = sampleTime;
	}

	public int getServiceId() {
		return serviceId;
	}

	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "History [id=" + id + ", gmtCreate=" + gmtCreate + ", sampleTime=" + sampleTime + ", serviceId="
				+ serviceId + ", state=" + state + ", totalSuccessTimes=" + totalSuccessTimes + ", totalFailTimes="
				+ totalFailTimes + ", todayTotalSuccessTimes=" + todayTotalSuccessTimes + ", todayTotalFailTimes="
				+ todayTotalFailTimes + ", responseBody=" + responseBody + "]";
	}

}
