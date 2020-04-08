package com.hanl.etl.api;

import java.util.Map;

/**
 * @author: Hanl
 * @date :2020/4/2
 * @desc:
 */
public class DebugeFileFlowListener implements FlowListener {

    private String filePath;

    public DebugeFileFlowListener(String filePath) {

        this.filePath = filePath;
    }

    @Override
    public void listen() {

    }

}
