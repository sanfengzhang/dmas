package com.hanl.dams.entity.ldm;

import com.hanl.dams.entity.pdm.MetaFieldEntity;
import lombok.Data;

/**
 * @author: Hanl
 * @date :2020/3/19
 * @desc:
 */
@Data
public class LogicDataMetaFieldRelation {

    private MetaFieldEntity metaField;

    private LogicDataModelEntity busDataType;
}
