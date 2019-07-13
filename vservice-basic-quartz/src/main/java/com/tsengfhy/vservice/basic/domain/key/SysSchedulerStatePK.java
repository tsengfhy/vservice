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
public class SysSchedulerStatePK implements Serializable {

    private static final long serialVersionUID = 8193793161613587512L;

    @Column(name = "sched_name", length = 120, nullable = false)
    private String schedName;

    @Column(name = "instance_name", length = 190, nullable = false)
    private String instanceName;
}
