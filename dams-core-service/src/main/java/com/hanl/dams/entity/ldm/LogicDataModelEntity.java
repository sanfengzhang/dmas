package com.hanl.dams.entity.ldm;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * @author: Hanl
 * @date :2020/3/19
 * @desc: 逻辑层数据模型 *
 *这里的逻辑数据模型不面向任何业务，具体的业务应该是基于这些逻辑模型实体去
 * 创建的业务流程。
 *
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
