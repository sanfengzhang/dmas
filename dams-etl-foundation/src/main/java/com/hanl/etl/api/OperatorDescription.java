package com.hanl.etl.api;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author: Hanl
 * @date :2020/1/16
 * @desc:
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface OperatorDescription {

    public static final String DESC_NAME = "name";

    public static final String OP_NAME = "opName";

    public static final String OP_TYPE = "opType";

    String descName();

    String opName();

    String opType();
}
