package com.hanl.dams.entity.pdm;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * @author: Hanl
 * @date :2020/3/17
 * @desc:
 */
@Data
public class MetaFieldEntity extends DataField {

    private int length;//字段长度

    private String analyse;//字段使用的分词器

    private String pattern;//字段格式匹配模型,时间类型:yyyyMMdd,或者是邮箱类型等

    private String traceSource;//字段溯源描述,描述该字段是如何生成的

    private String defaultValue;//当字段非法或者为空的时候可以使用的默认值

    private String masterDataField;//标记当前字段是主数据字段，master(config配置型主数据,kernel核心主数据)

    private String ruleDescription;//对于字段遵行的规则描述，例如主数据定一些数据统一为1开头的8位流水码等

    //通过这个可以知道字段被哪些业务模型所使用，可以用于计算数据字段的共享度、通过不同的业务模型可以知道
    //该数据字段在企业信息中价值评估、以及风险影响分析。这一类数据也可以成为企业的主数据
    //然后其他业务可以围绕主数据展开分析
    private Set<MetaFieldEntity> metaFieldSet = new HashSet<>();

}
