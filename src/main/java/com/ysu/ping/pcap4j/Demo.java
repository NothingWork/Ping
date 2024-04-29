package com.ysu.ping.pcap4j;

/**
 * @author Yun
 * @description: TODO
 */
public class Demo {
    public static void main(String[] args) throws Exception {
        SendPac sendUtil = new SendPac();
        CapPac capPac = new CapPac();
        capPac.start();
        sendUtil.sendArp("192.168.185.116");
    }
}
