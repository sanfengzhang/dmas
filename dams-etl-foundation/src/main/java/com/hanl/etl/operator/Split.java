package com.hanl.etl.operator;

import com.google.common.base.Splitter;
import com.hanl.etl.api.*;
import com.hanl.etl.exception.OperatorCompilationException;
import com.typesafe.config.Config;

import java.util.Iterator;
import java.util.List;

/**
 * @author: Hanl
 * @date :2020/3/8
 * @desc:
 */
@OperatorDescription(descName = "分隔符解析", opName = "split", opType = "正则解析")
public final class Split<T extends RecordWrapper> extends AbstractOperator<T> {

    @OperatorParamDescription(paramName = "inputFieldName", paramType = "java.lang.String", paramDisplayName = "输入字段名称")
    private final String inputFieldName;

    @OperatorParamDescription(paramName = "outputFieldName", paramType = "java.lang.String", paramDisplayName = "输出字段名称")
    private final String outputFieldName;

    @OperatorParamDescription(paramName = "outputFieldNames", paramType = "java.util.List", paramDisplayName = "输出字段列表")
    private final List<String> outputFieldNames;

    @OperatorParamDescription(paramName = "addEmptyStrings", paramType = "java.lang.Boolean", paramDisplayName = "添加空字符串")
    private final boolean addEmptyStrings;

    @OperatorParamDescription(paramName = "separator", paramType = "java.lang.String", paramDisplayName = "分割符")
    private final String separator;

    @OperatorParamDescription(paramName = "isRegex", paramType = "java.lang.Boolean", paramDisplayName = "是否支持正则")
    private final boolean isRegex;

    private final Splitter splitter;

    public Split(OperatorBuilder builder, Config config, Operator parent, Operator child, FlowContext context) {
        super(builder, config, parent, child, context);
        this.inputFieldName = getConfigs().getString(config, "inputField");

        this.outputFieldName = getConfigs().getString(config, "outputField", null);
        this.outputFieldNames = getConfigs().getStringList(config, "outputFields", null);
        if (outputFieldName == null && outputFieldNames == null) {
            throw new OperatorCompilationException("Either outputField or outputFields must be defined", config);
        }
        if (outputFieldName != null && outputFieldNames != null) {
            throw new OperatorCompilationException("Must not define both outputField and outputFields at the same time", config);
        }

        separator = getConfigs().getString(config, "separator");
        isRegex = getConfigs().getBoolean(config, "isRegex", false);
        GrokDictionaries dict = new GrokDictionaries(config, getConfigs());
        Splitter currentSplitter;
        if (isRegex) {
            currentSplitter = Splitter.on(dict.compileExpression(separator).pattern());
        } else if (separator.length() == 1) {
            currentSplitter = Splitter.on(separator.charAt(0));
        } else {
            currentSplitter = Splitter.on(separator);
        }

        int limit = getConfigs().getInt(config, "limit", -1);
        if (limit > 0) {
            currentSplitter = currentSplitter.limit(limit);
        }

        this.addEmptyStrings = getConfigs().getBoolean(config, "addEmptyStrings", false);
        if (outputFieldNames == null && !addEmptyStrings) {
            currentSplitter = currentSplitter.omitEmptyStrings();
        }

        if (getConfigs().getBoolean(config, "trim", true)) {
            currentSplitter = currentSplitter.trimResults();
        }

        this.splitter = currentSplitter;
        validateArguments();
    }

    @Override
    protected boolean doProcess(RecordWrapper record) {
        for (Object value : record.getArray(inputFieldName)) {
            Iterable<String> columns = splitter.split(value.toString());
            if (outputFieldNames == null) {
                record.putAll(outputFieldName, columns);
            } else {
                extractColumns(record, columns);
            }
        }
        return super.doProcess(record);
    }

    private void extractColumns(RecordWrapper record, Iterable<String> columns) {
        Iterator<String> iter = columns.iterator();
        for (int i = 0; i < outputFieldNames.size() && iter.hasNext(); i++) {
            String columnValue = iter.next();
            String columnName = outputFieldNames.get(i);
            if (columnName.length() > 0) { // empty column name indicates omit this field on output
                if (columnValue.length() > 0 || addEmptyStrings) {
                    record.put(columnName, columnValue);
                }
            }
        }
    }
}
