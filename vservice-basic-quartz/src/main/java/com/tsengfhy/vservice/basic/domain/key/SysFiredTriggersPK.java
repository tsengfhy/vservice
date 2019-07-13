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
public class SysFiredTriggersPK implements Serializable {

    private static final long serialVersionUID = 3935367982941541807L;

    @Column(name = "sched_name", length = 120, nullable = false)
    private String schedName;

    @Column(name = "entry_id", length = 95, nullable = false)
    private String entryId;
}
