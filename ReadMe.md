Pcap4j网络抓包和发包工具学习

这里记录了一些主要的API接口
本项目已整合springdoc自动生成Restful风格的api文档，请启动项目后在下面地址查看
http://127.0.0.1:8080/swagger-ui/index.html#/

| 接口            | 状态码 | msg     | data             |
|---------------|-----|---------|------------------|
| /getRouterMac | 101 | success | 成功获取到的路由MAC地址    |
| /getRouterMac | 201 | failed  | null             |
| /getRouterMac | 401 | error   | null             |
| /getLocalInfo | 102 | success | 成功获取到的本机ip和路由mac |
| /getLocalInfo | 202 | failed  | null             |
| /pingIp       | 103 | success | 收到目标ip的Ping响应信息  |
| /pingIp       | 203 | timeout | null             |
| /pingIp       | 402 | error   | null             |
