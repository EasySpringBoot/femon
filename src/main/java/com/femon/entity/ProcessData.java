package com.femon.entity;

import java.util.Date;
import javax.persistence.*;

/**
 * Created by boshu2 on 2016/2/27.
 */
@Entity
@Table(name = "fm_process_data")
public class ProcessData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "process_id")
    private int processId;

    @Column(name = "state")
    private int state;

    @Column(name = "sample_time")
    private Date sampleTime;

    @Column(name = "output")
    private String output;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getProcessId() {
        return processId;
    }
    public void setProcessId(int processId) {
        this.processId = processId;
    }

    public int getState() {
        return state;
    }
    public void setState(int state) {
        this.state = state;
    }

    public Date getSampleTime() {
        return sampleTime;
    }
    public void setSampleTime(Date sampleTime) {
        this.sampleTime = sampleTime;
    }

    public String getOutput() {
        return output;
    }
    public void setOutput(String output) {
        this.output = output;
    }
}
