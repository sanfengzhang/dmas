package com.hanl.etl.api;

import java.lang.annotation.*;

/**
 * @author: Hanl
 * @date :2020/1/15
 * @desc:
 */

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperatorParamDescription {

    public static final String PARAM_NAME = "paramName";

    public static final String PARAM_TYPE = "paramType";

    public static final String PARAM_DISPLAY_NAME = "paramDisplayName";

    String paramName();

    String paramType();

    String paramDisplayName() default "";

    String wirteValueOperation() default "";
}
