package com.femon.result;

import java.io.Serializable;

public class ServiceDataResult implements Serializable {

	private static final long serialVersionUID = 1L;
	private int serviceId;
	private int state;
	private String sampleTime;
	private String responseBody;

	public String getResponseBody() {
		return responseBody;
	}

	public void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
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

	public String getSampleTime() {
		return sampleTime;
	}

	public void setSampleTime(String sampleTime) {
		this.sampleTime = sampleTime;
	}

	@Override
	public String toString() {
		return "ResultHistory [serviceId=" + serviceId + ", state=" + state + ", sampleTime=" + sampleTime
				+ ", responseBody=" + responseBody + "]";
	}

}
