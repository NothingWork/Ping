package com.ysu.ping.Entity;

/**
 * @author Yun
 * @description: Ping结果的回复
 */
public class PingReply {
    private String ip;//响应的ip
    private long pingTime;//响应时间
    private String ttl;//生存时间

    public PingReply(String ip, long pingTime, String ttl) {
        this.ip = ip;
        this.pingTime = pingTime;
        this.ttl = ttl;
    }

    public PingReply() {

    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public long getPingTime() {
        return pingTime;
    }

    public void setPingTime(long pingTime) {
        this.pingTime = pingTime;
    }

    public String getTtl() {
        return ttl;
    }

    public void setTtl(String ttl) {
        this.ttl = ttl;
    }
}
