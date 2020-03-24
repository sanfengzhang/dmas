package com.hanl.dams.entity.pdm;

import lombok.Data;

/**
 * @author: Hanl
 * @date :2020/3/17
 * @desc:
 */
@Data
public class MetaFieldMetricEntity {

    private String metricName;//指标名称

    private String metricType;//指标类型,基础指标、组合指标

    private String metricCalculate;//指标计算:例如SQL查询统计、实时计算的还是？

    private String urn;//
}
