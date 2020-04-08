package com.hanl.etl.api;

import java.util.Map;

/**
 * @author: Hanl
 * @date :2020/4/2
 * @desc: 监听器，能通过监听器使得Flow执行一些操作
 * 比如:健康检查的启动和停止、流程调试的启动和停止
 */
public interface FlowListener {

    public void listen();
}
