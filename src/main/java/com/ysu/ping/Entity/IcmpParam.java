package com.ysu.ping.Entity;

/**
 * @author Yun
 * @description: Icmp数据包参数
 */
public class IcmpParam {
    private String srcIp;//源ip
    private String SrcMac;//源mac
    private String dstIp;//目标ip
    private String dstMac;//目标mac

    public IcmpParam(String srcIp, String srcMac, String dstIp, String dstMac) {
        this.srcIp = srcIp;
        SrcMac = srcMac;
        this.dstIp = dstIp;
        this.dstMac = dstMac;
    }

    public String getSrcIp() {
        return srcIp;
    }

    public void setSrcIp(String srcIp) {
        this.srcIp = srcIp;
    }

    public String getSrcMac() {
        return SrcMac;
    }

    public void setSrcMac(String srcMac) {
        SrcMac = srcMac;
    }

    public String getDstIp() {
        return dstIp;
    }

    public void setDstIp(String dstIp) {
        this.dstIp = dstIp;
    }

    public String getDstMac() {
        return dstMac;
    }

    public void setDstMac(String dstMac) {
        this.dstMac = dstMac;
    }
}
