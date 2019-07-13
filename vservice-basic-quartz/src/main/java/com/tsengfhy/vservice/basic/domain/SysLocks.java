package com.tsengfhy.vservice.basic.domain;

import com.tsengfhy.vservice.basic.domain.key.SysLocksPK;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "sys_locks", schema = "sys")
public class SysLocks implements Serializable {

    private static final long serialVersionUID = -8258785627945319913L;

    @Id
    private SysLocksPK id;
}
