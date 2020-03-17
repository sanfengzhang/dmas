package com.hanl.dams.entity;

import java.util.HashSet;
import java.util.Set;

/**
 * @author: Hanl
 * @date :2020/3/17
 * @desc: 存储数据类型，是在程序层面定义的数据类型。
 */
public class DataTypeEntity {

    private String dataTypeName;

    private Set<MetaFieldEntity> metaFields = new HashSet<>();

    private Set<DataTypeStoreEntity> stores = new HashSet<>();

    private Set<String> inputs =new HashSet<>();//这个input输入不好设计，不能预知有哪些可能，如何分类
}
