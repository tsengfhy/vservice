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
public class SysLocksPK implements Serializable {

    private static final long serialVersionUID = 185407936321265079L;

    @Column(name = "sched_name", length = 120, nullable = false)
    private String schedName;

    @Column(name = "lock_name", length = 40, nullable = false)
    private String lockName;
}
