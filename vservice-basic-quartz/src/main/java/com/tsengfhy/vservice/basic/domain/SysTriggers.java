package com.tsengfhy.vservice.basic.domain;

import com.tsengfhy.vservice.basic.domain.key.SysTriggersPK;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "sys_triggers", schema = "sys",
        indexes = {
                @Index(name = "IDX_SYS_T_J", columnList = "sched_name, job_name, job_group"),
                @Index(name = "IDX_SYS_T_JG", columnList = "sched_name, job_group"),
                @Index(name = "IDX_SYS_T_C", columnList = "sched_name, calendar_name"),
                @Index(name = "IDX_SYS_T_G", columnList = "sched_name, trigger_group"),
                @Index(name = "IDX_SYS_T_STATE", columnList = "sched_name, trigger_state"),
                @Index(name = "IDX_SYS_T_N_STATE", columnList = "sched_name, trigger_name, trigger_group, trigger_state"),
                @Index(name = "IDX_SYS_T_N_G_STATE", columnList = "sched_name, trigger_group, trigger_state"),
                @Index(name = "IDX_SYS_T_NEXT_FIRE_TIME", columnList = "sched_name, next_fire_time"),
                @Index(name = "IDX_SYS_T_NFT_ST", columnList = "sched_name, trigger_state, next_fire_time"),
                @Index(name = "IDX_SYS_T_NFT_MISFIRE", columnList = "sched_name, misfire_instr, next_fire_time"),
                @Index(name = "IDX_SYS_T_NFT_ST_MISFIRE", columnList = "sched_name, misfire_instr, next_fire_time, trigger_state"),
                @Index(name = "IDX_SYS_T_NFT_ST_MISFIRE_GRP", columnList = "sched_name, misfire_instr, next_fire_time, trigger_group, trigger_state")
        }
)
public class SysTriggers implements Serializable {

    private static final long serialVersionUID = 905890976525498865L;

    @Id
    private SysTriggersPK id;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumns({
            @JoinColumn(name = "sched_name", referencedColumnName = "sched_name", nullable = false, insertable = false, updatable = false),
            @JoinColumn(name = "job_name", referencedColumnName = "job_name", nullable = false, insertable = false, updatable = false),
            @JoinColumn(name = "job_group", referencedColumnName = "job_group", nullable = false, insertable = false, updatable = false)
    })
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private SysJobDetails sysJobDetails;

    @Column(name = "description", length = 250)
    private String description;

    @Column(name = "next_fire_time", length = 13)
    private Long nextFireTime;

    @Column(name = "prev_fire_time", length = 13)
    private Long prevFireTime;

    @Column(name = "priority")
    private Integer priority;

    @Column(name = "trigger_state", length = 16, nullable = false)
    private String triggerState;

    @Column(name = "trigger_type", length = 8, nullable = false)
    private String triggerType;

    @Column(name = "start_time", length = 13, nullable = false)
    private Long startTime;

    @Column(name = "end_time", length = 13)
    private Long endTime;

    @Column(name = "calendar_name", length = 190)
    private String calendarName;

    @Column(name = "misfire_instr", length = 2)
    private Short misfireInstr;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "job_data", columnDefinition = "BLOB")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private byte[] jobData;

    @OneToOne(mappedBy = "sysTriggers", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private SysBlobTriggers sysBlobTriggers;

    @OneToOne(mappedBy = "sysTriggers", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private SysCronTriggers sysCronTriggers;

    @OneToOne(mappedBy = "sysTriggers", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private SysSimpleTriggers sysSimpleTriggers;

    @OneToOne(mappedBy = "sysTriggers", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private SysSimpropTriggers sysSimpropTriggers;
}
