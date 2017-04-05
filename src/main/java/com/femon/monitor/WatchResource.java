package com.femon.monitor;

import com.femon.dao.ResourceDao;
import com.femon.dao.ResourceDataDao;
import com.femon.entity.Server;
import com.femon.entity.ResourceData;
import com.jcraft.jsch.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.femon.engine.RemoteShell;
import java.util.*;

/**
 * Created by boshu2 on 2016/2/24.
 */
@Component
public class WatchResource {
    @Autowired
    ResourceDao resourceDao;

    @Autowired
    ResourceDataDao resourceDataDao;

    public static final String DISK_FREE_COMMOND = "df -l | grep '/data' | awk '{print $4}'";
    public static final String CPU_FREE_COMMOND = "grep 'cpu ' /proc/stat | awk '{usage=($2+$4)*100/($2+$4+$5)} END {print usage \"%\"}'";
    public static final String MEM_FREE_COMMOND = "free -m | head -n2 | tail -n1 | awk '{print $4}'";

    @Scheduled(fixedRate = 60000)
    public void scheduleResource() {
        Iterable iterable = resourceDao.findAll();
        Iterator itor = iterable.iterator();
        List<Server> serverList = new ArrayList<Server>();
        for (; itor.hasNext();) {
            serverList.add((Server) itor.next());
        }
        if (serverList.isEmpty()) {
            return;
        }

        for (Server server : serverList) {
            RemoteShell rs = new RemoteShell();
            if (rs.connect(server.getUsername(), server.getPassword(), server.getIp())) {

                String diskShellResult = rs.runRemoteShell(DISK_FREE_COMMOND);
                String cpuShellResult = rs.runRemoteShell(CPU_FREE_COMMOND);
                String memShellResult = rs.runRemoteShell(MEM_FREE_COMMOND);

                if (diskShellResult == null || cpuShellResult == null || memShellResult == null) {
                    continue;
                }

                String resultDisk = diskShellResult.trim();
                String resultCpu = cpuShellResult.trim();
                String resultMem = memShellResult.trim();

                String resultCpuValue = resultCpu.substring(0, resultCpu.length() - 2);

                if (resultDisk.isEmpty() || resultCpu.isEmpty() || resultMem.isEmpty()) {
                    continue;
                }
                // System.out.println("===============================cpu=====================================:
                // "+resultCpuValue);
                // System.out.println("===============================mem=====================================:
                // "+resultMem);

                int diskAvail = Integer.valueOf(resultDisk);
                Double da = (double) (diskAvail / 1024 / 1024); // 转换成GB单位
                Double cpu = Double.valueOf(resultCpuValue);
                Double mem = Double.valueOf(resultMem); // 默认是MB

                if (null == da || null == cpu || null == mem) {
                    continue;
                }

                Integer id = server.getId();
                ResourceData resourceData = new ResourceData();

                resourceData.setServerId(id);
                resourceData.setDiskAvail(da);
                resourceData.setCpuAvail(cpu);
                resourceData.setMemAvail(mem);
                resourceData.setSampleTime(new Date());

                server.setState(1);

                resourceDataDao.save(resourceData);
                resourceDao.save(server);
            }
            rs.disconnect();
        }
    }
}
