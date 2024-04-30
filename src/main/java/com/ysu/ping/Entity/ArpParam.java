package com.ysu.ping.Entity;

/**
 * @author Yun
 * @description: 发送Arp数据包所要填写的参数
 */
public class ArpParam {
    private String dstIp;//目标ip地址
    private String srcIp;//源ip地址
    private String srcMac;//源MAC地址

    public ArpParam(String dstIp, String srcIp, String srcMac) {
        this.dstIp = dstIp;
        this.srcIp = srcIp;
        this.srcMac = srcMac;
    }

    public String getDstIp() {
        return dstIp;
    }

    public void setDstIp(String dstIp) {
        this.dstIp = dstIp;
    }

    public String getSrcIp() {
        return srcIp;
    }

    public void setSrcIp(String srcIp) {
        this.srcIp = srcIp;
    }

    public String getSrcMac() {
        return srcMac;
    }

    public void setSrcMac(String srcMac) {
        this.srcMac = srcMac;
    }
}
