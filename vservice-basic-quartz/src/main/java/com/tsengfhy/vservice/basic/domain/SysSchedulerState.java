package com.tsengfhy.vservice.basic.domain;

import com.tsengfhy.vservice.basic.domain.key.SysSchedulerStatePK;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "sys_scheduler_state", schema = "sys")
public class SysSchedulerState implements Serializable {

    private static final long serialVersionUID = 6327942765988730669L;

    @Id
    private SysSchedulerStatePK id;

    @Column(name = "last_checkin_time", length = 13, nullable = false)
    private Long lastCheckinTime;

    @Column(name = "checkin_interval", length = 13, nullable = false)
    private Long checkinInterval;
}
