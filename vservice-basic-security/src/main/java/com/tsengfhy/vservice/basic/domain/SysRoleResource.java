package com.tsengfhy.vservice.basic.domain;

import com.tsengfhy.vservice.basic.domain.key.SysRoleResourcePK;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "sys_role_resource", schema = "sys",
        indexes = {
                @Index(name = "IDX_SYS_ROLE_RESOURCE_ROLEID", columnList = "role_id"),
                @Index(name = "IDX_SYS_CLIENT_ROLE_RESOURCEID", columnList = "resource_id"),
        }
)
public class SysRoleResource implements Serializable {

    private static final long serialVersionUID = -8550934539747243785L;

    @Id
    private SysRoleResourcePK id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private SysRole sysRole;


    @ManyToOne(optional = false)
    @JoinColumn(name = "resource_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private SysResource sysResource;
}
