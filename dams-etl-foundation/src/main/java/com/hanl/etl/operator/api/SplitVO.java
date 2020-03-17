package com.hanl.etl.operator.api;

public class SplitVO implements OperatorParamVO {

    public java.lang.String inputField;

    public java.lang.String outputField;

    public java.util.List outputFields;

    public java.lang.Boolean addEmptyStrings;

    public java.lang.String separator;

    public java.lang.Boolean isRegex;


    @Override
    public String name() {
        return "split";
    }
}
