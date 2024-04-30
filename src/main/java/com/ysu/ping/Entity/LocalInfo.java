package com.ysu.ping.Entity;

/**
 * @author Yun
 * @description: 本机信息
 */
public class LocalInfo {
    private String localIP;// 本机ip
    private String localMac;// 本机Mac

    public LocalInfo(String localIP, String localMac) {
        this.localIP = localIP;
        this.localMac = localMac;
    }

    public String getLocalIP() {
        return localIP;
    }

    public void setLocalIP(String localIP) {
        this.localIP = localIP;
    }

    public String getLocalMac() {
        return localMac;
    }

    public void setLocalMac(String localMac) {
        this.localMac = localMac;
    }
}
