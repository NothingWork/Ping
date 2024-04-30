package com.ysu.ping.Entity;

/**
 * @author Yun
 * @description: Ping结果的回复
 */
public class PingReply {
    private String ip;//响应的ip
    private long time;//响应时间
    private String ttL;//生存时间

    public PingReply(String ip, long time, String ttL) {
        this.ip = ip;
        this.time = time;
        this.ttL = ttL;
    }

    public PingReply() {

    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getTtL() {
        return ttL;
    }

    public void setTtL(String ttL) {
        this.ttL = ttL;
    }
}
