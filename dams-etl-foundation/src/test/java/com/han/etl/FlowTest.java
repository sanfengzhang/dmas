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

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author: Hanl
 * @date :2020/3/6
 * @desc:
 */
public class FlowTest {

    @Test
    public void testMonth() throws Exception {

        String date = "201609";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
        System.out.println(dateFormat.parse(date));

        List<String> list = new ArrayList<>();
        list.add("111");
        list.add("222");
        list.add("333");
        list.add("444");
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            String a = it.next();
            a = "aaa";
        }

        System.out.println(list.toString());
    }

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
        double count = 5000_0000.0D;
        for (int i = 0; i < count; i++) {
            mockProcess(flow, collectOperator);
        }
        long end = System.currentTimeMillis();
        System.out.println(end-start);
        System.out.println(count/((end - start)/1000.0D)) ;
    }

    private void mockProcess(Flow<MapRecordWrapper> flow, CollectOperator<MapRecordWrapper> collectOperator) {
        MapRecordWrapper record = new MapRecordWrapper();
        record.put("message", "Foreigners in China get equal treatment|12|Foreigners in China get equal treatment|Foreigners in China get equal treatment|Foreigners in China get equal treatment|Foreigners in China get equal treatment");
        flow.process(record);
        collectOperator.reset();

        //TODO ArrayList使用ForEach有性能效率问题
        /**  for (RecordWrapper recordWrapper : result) {
         System.out.println(recordWrapper.toString());
         } **/
    }
}
