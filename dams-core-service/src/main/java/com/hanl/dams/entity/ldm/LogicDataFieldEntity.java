package com.hanl.dams.entity.ldm;

import lombok.Data;

/**
 * @author: Hanl
 * @date :2020/3/24
 * @desc:
 */
@Data
public class LogicDataFieldEntity {

    /**
     * 逻辑数据模型中字段名称
     */
    private String logicDataFieldName;

    /**
     * 数据字段的约束规范:数字、字符串、以1开头的序列号、值映射等
     */
    private String constraintSpec;

    /**
     * 对该数据字段的额外描述说明
     */
    private String description;

}
