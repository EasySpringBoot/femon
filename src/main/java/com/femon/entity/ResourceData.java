package com.femon.entity;

import java.util.Date;

import javax.persistence.*;

/**
 * Created by boshu2 on 2016/2/24.
 */
@Entity
@Table(name = "fm_resource_data")
public class ResourceData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "server_id")
    private Integer serverId;

    @Column(name = "disk_avail")
    private Double diskAvail;

    @Column(name = "cpu_avail")
    private Double cpuAvail;

    @Column(name = "mem_avail")
    private Double memAvail;

    @Column(name = "sample_time")
    private Date sampleTime;

    /**
     * id.
     * 
     * @return the id
     * @since JDK 1.7
     */
    public int getId() {
        return id;
    }

    /**
     * id.
     * 
     * @param id
     *            the id to set
     * @since JDK 1.7
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * serverId.
     * 
     * @return the serverId
     * @since JDK 1.7
     */
    public Integer getServerId() {
        return serverId;
    }

    /**
     * serverId.
     * 
     * @param serverId
     *            the serverId to set
     * @since JDK 1.7
     */
    public void setServerId(Integer serverId) {
        this.serverId = serverId;
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
     * cpuAvail.
     * 
     * @return the cpuAvail
     * @since JDK 1.7
     */
    public Double getCpuAvail() {
        return cpuAvail;
    }

    /**
     * cpuAvail.
     * 
     * @param cpuAvail
     *            the cpuAvail to set
     * @since JDK 1.7
     */
    public void setCpuAvail(Double cpuAvail) {
        this.cpuAvail = cpuAvail;
    }

    /**
     * memAvail.
     * 
     * @return the memAvail
     * @since JDK 1.7
     */
    public Double getMemAvail() {
        return memAvail;
    }

    /**
     * memAvail.
     * 
     * @param memAvail
     *            the memAvail to set
     * @since JDK 1.7
     */
    public void setMemAvail(Double memAvail) {
        this.memAvail = memAvail;
    }

    /**
     * sampleTime.
     * 
     * @return the sampleTime
     * @since JDK 1.7
     */
    public Date getSampleTime() {
        return sampleTime;
    }

    /**
     * sampleTime.
     * 
     * @param sampleTime
     *            the sampleTime to set
     * @since JDK 1.7
     */
    public void setSampleTime(Date sampleTime) {
        this.sampleTime = sampleTime;
    }

}
