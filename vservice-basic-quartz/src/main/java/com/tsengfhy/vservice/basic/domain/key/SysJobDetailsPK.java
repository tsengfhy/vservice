package com.tsengfhy.vservice.basic.domain.key;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class SysJobDetailsPK implements Serializable {

    private static final long serialVersionUID = -2625405955502434161L;

    @Column(name = "sched_name", length = 120, nullable = false)
    private String schedName;

    @Column(name = "job_name", length = 190, nullable = false)
    private String jobName;

    @Column(name = "job_group", length = 190, nullable = false)
    private String jobGroup;
}
