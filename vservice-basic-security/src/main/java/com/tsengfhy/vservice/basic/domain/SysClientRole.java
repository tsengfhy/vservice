package com.tsengfhy.vservice.basic.domain;

import com.tsengfhy.vservice.basic.domain.key.SysClientRolePK;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "sys_client_role", schema = "sys",
        indexes = {
                @Index(name = "IDX_SYS_CLIENT_ROLE_CLIENTID", columnList = "client_id"),
                @Index(name = "IDX_SYS_CLIENT_ROLE_ROLEID", columnList = "role_id"),
        }
)
public class SysClientRole implements Serializable {

    private static final long serialVersionUID = 2249415795334112957L;

    @Id
    private SysClientRolePK id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private SysClient sysClient;

    @ManyToOne(optional = false)
    @JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private SysRole sysRole;
}
