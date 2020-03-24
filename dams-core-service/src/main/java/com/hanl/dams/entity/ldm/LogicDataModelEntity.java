package com.hanl.dams.entity.ldm;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * @author: Hanl
 * @date :2020/3/19
 * @desc: 业务逻辑层数据模型
 * 逻辑模型主要面对业务、部门、企业级别的数据，这些数据有着清晰的定义和描述
 */
@Data
public class LogicDataModelEntity {

    private String id;

    /**
     * 数据逻辑模型的名称
     */
    private String logicDataModelName;

    /**
     * 默认为null,非主数据，标记当前数据类型实体是master(config配置型主数据,kernel核心主数据)
     */
    private String masterData;

    /**
     * 唯一主键名称
     */
    private String pkName;

    /**
     * 外键名称{数据逻辑模型id:[],数据逻辑模型id:[]},关系描述一本分为
     * 无关联、一对一、一对多、多对多等关系
     */
    private String fkName;


    private Set<LogicDataFieldEntity> logicDataFieldSet = new HashSet<>();


}
