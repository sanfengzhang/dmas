package com.hanl.etl.operator;

import com.hanl.etl.api.*;
import com.typesafe.config.Config;

import java.util.Arrays;
import java.util.List;

/**
 * A morphline has a name and contains a chain of zero or more commands, through which the morphline
 * pipes each input record. A command transforms the record into zero or more records.
 */
@OperatorDescription(descName = "流程", opName =  "Flow", opType = "流程")
public final class Flow<T extends RecordWrapper> extends AbstractOperator<T> {

    @OperatorParamDescription(paramName = "id", paramType = "java.lang.String", paramDisplayName = "流程ID")
    private final String id;

    private final Operator<T> realChild;

    public Flow(OperatorBuilder builder, Config config, Operator<T> parent, Operator<T> child, FlowContext context) {
        super(builder, config, parent, child, context);
        this.id = getConfigs().getString(config, "id");

        List<String> importCommandSpecs = getConfigs().getStringList(config, "importCommands",
                Arrays.asList("com.**", "org.**", "net.**"));
        context.importOperatorBuilders(importCommandSpecs);

        getConfigs().getConfigList(config, "operators", null);
        List<Operator> childCommands = buildOperatorChain(config, "operators", child, false);
        if (childCommands.size() > 0) {
            this.realChild = childCommands.get(0);
        } else {
            this.realChild = child;
        }
        validateArguments();
    }

    @Override
    protected Operator<T> getChild() {
        return realChild;
    }

}
