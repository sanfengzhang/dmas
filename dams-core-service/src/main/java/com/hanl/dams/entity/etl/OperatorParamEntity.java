package com.hanl.dams.entity.etl;

import lombok.Data;

/**
 * @author: Hanl
 * @date :2020/3/17
 * @desc:算子参数实体
 */
@Data
public class OperatorParamEntity {

    private String id;

    private String paramName;//参数名称

    private String paramType;//参数类型

    private String paramAlias;//参数别名

    private String pattern;//参数校验,主要是在要求用户传递符合要求的参数，减少运行时异常
}
