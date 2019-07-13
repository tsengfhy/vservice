package com.tsengfhy.vservice.basic.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "sys_simprop_triggers", schema = "sys")
public class SysSimpropTriggers implements Serializable {
    private static final long serialVersionUID = 1626204139827789012L;

    @Id
    @OneToOne(cascade = CascadeType.REFRESH, optional = false)
    @JoinColumns({
            @JoinColumn(name = "sched_name", referencedColumnName = "sched_name", nullable = false),
            @JoinColumn(name = "trigger_name", referencedColumnName = "trigger_name", nullable = false),
            @JoinColumn(name = "trigger_group", referencedColumnName = "trigger_group", nullable = false)
    })
    private SysTriggers sysTriggers;

    @Column(name = "str_prop_1", length = 512)
    private String strProp1;

    @Column(name = "str_prop_2", length = 512)
    private String strProp2;

    @Column(name = "str_prop_3", length = 512)
    private String strProp3;

    @Column(name = "int_prop_1")
    private Integer intProp1;

    @Column(name = "int_prop_2")
    private Integer intProp2;

    @Column(name = "long_prop_1")
    private Long longProp1;

    @Column(name = "long_prop_2")
    private Long longProp2;

    @Column(name = "dec_prop_1", columnDefinition = "NUMERIC(13,4)")
    private BigDecimal decProp1;

    @Column(name = "dec_prop_2", columnDefinition = "NUMERIC(13,4)")
    private BigDecimal decProp2;

    @Column(name = "bool_prop_1", length = 1)
    private String boolProp1;

    @Column(name = "bool_prop_2", length = 1)
    private String boolProp2;
}
