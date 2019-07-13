package com.tsengfhy.vservice.basic.domain;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "sys_simple_triggers", schema = "sys")
public class SysSimpleTriggers implements Serializable {

    private static final long serialVersionUID = 7668176142816705661L;

    @Id
    @OneToOne(cascade = CascadeType.REFRESH, optional = false)
    @JoinColumns({
            @JoinColumn(name = "sched_name", referencedColumnName = "sched_name", nullable = false),
            @JoinColumn(name = "trigger_name", referencedColumnName = "trigger_name", nullable = false),
            @JoinColumn(name = "trigger_group", referencedColumnName = "trigger_group", nullable = false)
    })
    private SysTriggers sysTriggers;

    @Column(name = "repeat_count", length = 7, nullable = false)
    private Long repeatCount;

    @Column(name = "repeat_interval", length = 12, nullable = false)
    private Long repeatInterval;

    @Column(name = "times_triggered", length = 10, nullable = false)
    private Long timesTriggered;
}
