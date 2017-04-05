package com.femon.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by boshu2 on 2016/2/25.
 */
@Entity
@Table(name = "fm_process")
public class Process {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private int serverId;
    private String name;
    private Date gmtCreate;
    private int state;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getServerId() {
        return serverId;
    }
    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }
    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public int getState() {
        return state;
    }
    public void setState(int state) {
        this.state = state;
    }
}
