package com.tsengfhy.vservice.basic.domain;

import com.tsengfhy.vservice.basic.domain.key.SysUserRolePK;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "sys_user_role", schema = "sys",
        indexes = {
                @Index(name = "IDX_SYS_USER_ROLE_USERID", columnList = "user_id"),
                @Index(name = "IDX_SYS_USER_ROLE_ROLEID", columnList = "role_id"),
        }
)
public class SysUserRole implements Serializable {

    private static final long serialVersionUID = -3643040237040486404L;

    @Id
    private SysUserRolePK id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private SysUser sysUser;

    @ManyToOne(optional = false)
    @JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private SysRole sysRole;
}
