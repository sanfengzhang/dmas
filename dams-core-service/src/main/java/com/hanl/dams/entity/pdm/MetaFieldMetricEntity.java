package com.hanl.dams.entity.pdm;

import lombok.Data;

/**
 * @author: Hanl
 * @date :2020/3/17
 * @desc:
 * 需要将指标下沉到数据层，独立的作为维表层；一般有维度就需要有统计聚合等计算操作。
 * 如何将维表模型设计出来，指标来源肯定是通过一张或多张"表"计算出来的；结合技术实现
 * 一般维度较好的kylin,druid等开源引擎，基于这些基础的技术可以作为指标计算的引擎。
 * 那么如何通过良好的维度建模让维表层具有更好的扩展性？
 *
 */
@Data
public class MetaFieldMetricEntity {

    private String metricName;//指标名称

    private String metricType;//指标类型,基础指标、组合指标

    private String metricCalculate;//指标计算:例如SQL查询统计、实时计算的还是？

    private String urn;//
}
