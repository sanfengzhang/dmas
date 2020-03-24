package com.hanl.dams.mdm.service;

import com.hanl.dams.entity.ldm.LogicDataModelEntity;

import java.util.List;

/**
 * @author: Hanl
 * @date :2020/3/23
 * @desc:
 */
public interface MasterDataService {

    /**
     * 获取所有的主数据模型实体信息
     *注意：
     * @return
     */
    public List<LogicDataModelEntity> getMasterDataSet();


    /**
     * 创建一个主数据对象，
     * @param busDataTypeEntity
     */
    public void addMasterData(LogicDataModelEntity busDataTypeEntity);
}
