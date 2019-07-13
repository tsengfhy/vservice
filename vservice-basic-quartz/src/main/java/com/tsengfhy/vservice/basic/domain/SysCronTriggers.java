package com.tsengfhy.vservice.basic.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "sys_cron_triggers", schema = "sys")
public class SysCronTriggers implements Serializable {

    private static final long serialVersionUID = 5939316719990371331L;

    @Id
    @OneToOne(cascade = CascadeType.REFRESH, optional = false)
    @JoinColumns({
            @JoinColumn(name = "sched_name", referencedColumnName = "sched_name", nullable = false, insertable = false, updatable = false),
            @JoinColumn(name = "trigger_name", referencedColumnName = "trigger_name", nullable = false, insertable = false, updatable = false),
            @JoinColumn(name = "trigger_group", referencedColumnName = "trigger_group", nullable = false, insertable = false, updatable = false)
    })
    private SysTriggers sysTriggers;

    @Column(name = "cron_expression", length = 120, nullable = false)
    private String cronExpression;

    @Column(name = "time_zone_id", length = 80)
    private String timeZoneId;
}
