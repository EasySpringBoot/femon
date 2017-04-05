package com.femon.result;

import java.io.Serializable;

public class ServiceDataStatisticResult implements Serializable {

	private int serviceId;
	private String serviceName;
	private String day;
	private int failTimes;
	private int successTimes;
	private int totalTimes;

	public int getServiceId() {
		return serviceId;
	}

	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public int getFailTimes() {
		return failTimes;
	}

	public void setFailTimes(int failTimes) {
		this.failTimes = failTimes;
	}

	public int getSuccessTimes() {
		return successTimes;
	}

	public void setSuccessTimes(int successTimes) {
		this.successTimes = successTimes;
	}

	public int getTotalTimes() {
		return totalTimes;
	}

	public void setTotalTimes(int totalTimes) {
		this.totalTimes = totalTimes;
	}

	@Override
	public String toString() {
		return "ResultDataStatistic [serviceId=" + serviceId + ", serviceName=" + serviceName + ", day=" + day
				+ ", failTimes=" + failTimes + ", successTimes=" + successTimes + ", totalTimes=" + totalTimes + "]";
	}

}
