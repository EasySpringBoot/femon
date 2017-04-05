package com.femon.engine;

import com.jcraft.jsch.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Created by boshu2 on 2016/2/27.
 */
public class RemoteShell {

    private  Session session;
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public  String runRemoteShell(String command) {
        StringBuilder stringBuffer;
        String result = null;
        BufferedReader reader = null;
        Channel channel = null;
        try {
            stringBuffer = new StringBuilder();
            channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);
            channel.connect();

            InputStream in = channel.getInputStream();
            reader = new BufferedReader(new InputStreamReader(in));
            String buf;
            while ((buf = reader.readLine()) != null) {
                stringBuffer.append(buf.trim()).append(LINE_SEPARATOR);
            }
            result = stringBuffer.toString();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (channel != null)
                channel.disconnect();
        }
        return result;
    }

    public  boolean connect(String user, String passwd, String host) {
        JSch jsch = new JSch();
        try {
            session = jsch.getSession(user, host, 22);
            session.setPassword(passwd);
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
        } catch (JSchException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void disconnect() {
        try {
            session.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
