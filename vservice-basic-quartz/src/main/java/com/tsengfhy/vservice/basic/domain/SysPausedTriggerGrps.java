package com.tsengfhy.vservice.basic.domain;

import com.tsengfhy.vservice.basic.domain.key.SysPausedTriggerGrpsPK;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "sys_paused_trigger_grps", schema = "sys")
public class SysPausedTriggerGrps implements Serializable {

    private static final long serialVersionUID = 5026580762089234339L;

    @Id
    private SysPausedTriggerGrpsPK id;
}
