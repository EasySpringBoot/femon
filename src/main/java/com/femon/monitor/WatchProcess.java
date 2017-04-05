package com.femon.monitor;

import com.femon.dao.ProcessDao;
import com.femon.dao.ProcessDataDao;
import com.femon.dao.ResourceDao;
import com.femon.engine.RemoteShell;
import com.femon.entity.Process;
import com.femon.entity.ProcessData;
import com.femon.entity.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by boshu2 on 2016/2/27.
 */
@Component
public class WatchProcess {
    @Autowired
    ProcessDao processDao;

    @Autowired
    ProcessDataDao processDataDao;

    @Autowired
    ResourceDao resourceDao;

    @Scheduled(fixedRate = 60000)
    public void scheduleProcess() {
        Iterator itor = processDao.findAll().iterator();
        List<Process> processes = new ArrayList<Process>();
        for (; itor.hasNext();) {
            processes.add((Process) itor.next());
        }

        if (processes.isEmpty())
            return;

        for (Process process : processes) {
            RemoteShell rs = new RemoteShell();
            int serverId = process.getServerId();
            Server server = resourceDao.findOne(serverId);
            if (!rs.connect(server.getUsername(), server.getPassword(), server.getIp())) {
                rs.disconnect();
                continue;

            } else {
                String PROCESS_COMMAND = "ps  -ef | grep '" + process.getName() + "'";
                String commandResult = rs.runRemoteShell(PROCESS_COMMAND);
                if (commandResult == null || commandResult.isEmpty()) {
                    rs.disconnect();
                    continue;
                }

                String[] singleStr = commandResult.split("\n");

                ProcessData processData = new ProcessData();
                processData.setProcessId(process.getId());
                if (singleStr.length > 2) {
                    processData.setState(1); // 成功
                    process.setState(1);
                } else {
                    processData.setState(0); // 失败
                    process.setState(0);
                }
                processData.setSampleTime(new Date());
                processData.setOutput(commandResult);
                processDataDao.save(processData);
                processDao.save(process);

            }
            rs.disconnect();
        }
    }
}
