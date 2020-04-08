package com.hanl.dams.entity.etl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author: Hanl
 * @date :2020/3/17
 * @desc: 算子模型
 */
@Data
@Entity
@Table(name = "tb_operator")
@EqualsAndHashCode(of = {"id"})
public class OperatorClazzEntity {

    @Id
    @GenericGenerator(name = "jpa-uuid", strategy = "uuid")
    @GeneratedValue(generator = "jpa-uuid")
    private String id;

    @Column(name = "op_name")
    private String operatorName;//算子的名称：split,Flow等

    @Column(name = "op_alias")
    private String operatorAlias; //算子别名

    @Column(name = "op_type")
    private String operatorType;//算子类型:解析规则、富化规则等

    @Column(name = "op_clazz")
    private String operatorClazz;//算子的全类名

    @Column(name = "op_provider")
    private String provider;//算子的提供者

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "operator", cascade = {CascadeType.MERGE})
    @JsonIgnoreProperties(value = {"operator"})
    private Set<OperatorParamEntity> operatorParamSet = new HashSet<>();


}
