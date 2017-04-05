package com.femon.result;

import java.io.Serializable;

public class ResourceDataResult implements Serializable {

    private static final long serialVersionUID = 1L;
    private int resourceId;
    private Double diskAvail;
    private Double cpuUsage;
    private Double memFree;
    private String sampleTime;

    /**
     * resourceId.
     * 
     * @return the resourceId
     * @since JDK 1.7
     */
    public int getResourceId() {
        return resourceId;
    }

    /**
     * resourceId.
     * 
     * @param resourceId
     *            the resourceId to set
     * @since JDK 1.7
     */
    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    /**
     * diskAvail.
     * 
     * @return the diskAvail
     * @since JDK 1.7
     */
    public Double getDiskAvail() {
        return diskAvail;
    }

    /**
     * diskAvail.
     * 
     * @param diskAvail
     *            the diskAvail to set
     * @since JDK 1.7
     */
    public void setDiskAvail(Double diskAvail) {
        this.diskAvail = diskAvail;
    }

    /**
     * cpuUsage.
     * 
     * @return the cpuUsage
     * @since JDK 1.7
     */
    public Double getCpuUsage() {
        return cpuUsage;
    }

    /**
     * cpuUsage.
     * 
     * @param cpuUsage
     *            the cpuUsage to set
     * @since JDK 1.7
     */
    public void setCpuUsage(Double cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    /**
     * memFree.
     * 
     * @return the memFree
     * @since JDK 1.7
     */
    public Double getMemFree() {
        return memFree;
    }

    /**
     * memFree.
     * 
     * @param memFree
     *            the memFree to set
     * @since JDK 1.7
     */
    public void setMemFree(Double memFree) {
        this.memFree = memFree;
    }

    /**
     * sampleTime.
     * 
     * @return the sampleTime
     * @since JDK 1.7
     */
    public String getSampleTime() {
        return sampleTime;
    }

    /**
     * sampleTime.
     * 
     * @param sampleTime
     *            the sampleTime to set
     * @since JDK 1.7
     */
    public void setSampleTime(String sampleTime) {
        this.sampleTime = sampleTime;
    }

    /**
     * TODO
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ResourceDataResult [resourceId=" + resourceId + ", diskAvail=" + diskAvail + ", cpuUsage=" + cpuUsage
                + ", memFree=" + memFree + ", sampleTime=" + sampleTime + "]";
    }

}
