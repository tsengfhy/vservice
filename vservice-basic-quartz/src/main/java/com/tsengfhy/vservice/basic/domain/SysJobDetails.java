package com.tsengfhy.vservice.basic.domain;

import com.tsengfhy.vservice.basic.domain.key.SysJobDetailsPK;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

@Data
@Entity
@Table(name = "sys_job_details", schema = "sys",
        indexes = {
                @Index(name = "IDX_SYS_J_REQ_RECOVERY", columnList = "sched_name, requests_recovery"),
                @Index(name = "IDX_SYS_J_GRP", columnList = "sched_name, job_group")
        }
)
public class SysJobDetails implements Serializable {

    private static final long serialVersionUID = -2593711694301254891L;

    @Id
    private SysJobDetailsPK id;

    @Column(name = "description", length = 250)
    private String description;

    @Column(name = "job_class_name", length = 250, nullable = false)
    private String jobClassName;

    @Column(name = "is_durable", length = 1, nullable = false)
    private String idDurable;

    @Column(name = "is_nonconcurrent", length = 1, nullable = false)
    private String isNonconcurrent;

    @Column(name = "is_update_data", length = 1, nullable = false)
    private String isUpdateData;

    @Column(name = "requests_recovery", length = 1, nullable = false)
    private String requestsRecovery;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "job_data", columnDefinition = "BLOB")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private byte[] jobData;

    @OneToMany(mappedBy = "sysJobDetails", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Collection<SysTriggers> sysTriggers = new HashSet<>();
}
