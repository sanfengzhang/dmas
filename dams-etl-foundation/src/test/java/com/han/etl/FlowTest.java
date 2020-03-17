package com.han.etl;

import com.alibaba.fastjson.JSON;
import com.codahale.metrics.SharedMetricRegistries;
import com.hanl.etl.api.FlowContext;
import com.hanl.etl.api.RecordWrapper;
import com.hanl.etl.base.Compiler;
import com.hanl.etl.base.FaultTolerance;
import com.hanl.etl.base.MapRecordWrapper;
import com.hanl.etl.operator.CollectOperator;
import com.hanl.etl.operator.Flow;
import com.hanl.etl.operator.api.FlowVO;
import com.hanl.etl.operator.api.SplitVO;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Test;

import java.util.*;

/**
 * @author: Hanl
 * @date :2020/3/6
 * @desc:
 */
public class FlowTest {

    @Test
    public void buildFlowByMapRecord() {
        FaultTolerance faultTolerance = new FaultTolerance(false, false);
        FlowContext flowContext = new FlowContext.Builder().setExceptionHandler(faultTolerance)
                .setMetricRegistry(SharedMetricRegistries.getOrCreate("testId")).build();
        Map<String, Object> configMap = new HashMap<>();
        configMap.put("id", "testFlow");
        Map<String, Object> splitConfigMap = new HashMap<>();
        splitConfigMap.put("split", new HashMap<>());
        configMap.put("operators", Collections.singletonList(splitConfigMap));
        CollectOperator<RecordWrapper> collectOperator = new CollectOperator<RecordWrapper>();
        Flow<MapRecordWrapper> flow = new Compiler<MapRecordWrapper>().compile(configMap, flowContext, collectOperator);
        MapRecordWrapper record = new MapRecordWrapper();
        record.put("name", "zhangsan");
        boolean success = flow.process(record);
        collectOperator.getRecords().forEach(record1 -> {
            System.out.println(record1);
        });
        System.out.println(success);
    }

    @Test
    public void buildFlowByMapRecordParam() {
        PropertyConfigurator.configure("src/main/resources/log4j.properties");
        FlowVO flowVO = new FlowVO();
        flowVO.id = "test";
        SplitVO splitVO = new SplitVO();
        splitVO.inputField = "message";
        splitVO.separator = "|";
        List<String> list = new ArrayList<>();
        list.add("name");
        list.add("age");
        splitVO.outputFields = list;

        flowVO.add(splitVO);
        System.out.println(flowVO);
        System.out.println(JSON.toJSONString(flowVO));

        FaultTolerance faultTolerance = new FaultTolerance(false, false);
        FlowContext flowContext = new FlowContext.Builder().setExceptionHandler(faultTolerance)
                .setMetricRegistry(SharedMetricRegistries.getOrCreate("testId")).build();

        CollectOperator<MapRecordWrapper> collectOperator = new CollectOperator<MapRecordWrapper>();
        Flow<MapRecordWrapper> flow = new Compiler<MapRecordWrapper>().compile(JSON.parseObject(JSON.toJSONString(flowVO), Map.class), flowContext, collectOperator);

        long start = System.currentTimeMillis();
        MapRecordWrapper record = new MapRecordWrapper();
        record.put("message", "zhangsan|12");
        boolean success = flow.process(record);
        List<RecordWrapper> result = collectOperator.getRecords();

        //TODO ArrayList使用ForEach有性能效率问题
        for (RecordWrapper recordWrapper : result) {
            System.out.println(recordWrapper.toString());
        }
        System.out.println(success);
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }
}
