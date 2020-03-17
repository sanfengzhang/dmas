package com.hanl.etl.operator.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlowVO implements OperatorParamVO {

    public java.lang.String id;

    public List<Map<String, OperatorParamVO>> operators = new ArrayList<>();

    public void add(OperatorParamVO paramVO) {
        Map<String, OperatorParamVO> result = new HashMap<>();
        result.put(paramVO.name(), paramVO);
        operators.add(result);
    }

    @Override
    public String name() {
        return "Flow";
    }
}
