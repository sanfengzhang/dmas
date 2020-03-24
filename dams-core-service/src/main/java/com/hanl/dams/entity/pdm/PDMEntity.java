package com.hanl.dams.entity.pdm;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * @author: Hanl
 * @date :2020/3/17
 * @desc: 存储数据类型，是在程序层面定义的数据类型。
 * 需要定义数据以下几个方面的属性：
 * 1.高价值性:描述企业最核心的数据，是企业最有价值的数据资产
 * 2.数据共享性：可以根据有哪些部门、业务共享这份数据
 * 3.有效性:根据其实际应用分析中数据使用周期
 */
@Data
public class PDMEntity {

    private String dataTypeName;

    private Set<MetaFieldEntity> metaFields = new HashSet<>();

    private Set<PDMStoreEntity> stores = new HashSet<>();

    private Set<String> inputs = new HashSet<>();//这个input输入不好设计，不能预知有哪些可能，如何分类
}
