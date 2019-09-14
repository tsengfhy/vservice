package com.tsengfhy.vservice.basic.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "sys_client", schema = "sys",
        indexes = {
                @Index(name = "IDX_SYS_CLIENT_NAME", columnList = "name"),
        }
)
public class SysClient extends AbstractEntity {

    @Column(name = "secret", length = 100)
    private String secret;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "grant_types", length = 100)
    private String grantTypes;

    @Column(name = "scopes", length = 500)
    private String scopes;

    @Column(name = "approve_scopes", length = 500)
    private String approveScopes;

    @Column(name = "resources", length = 500)
    private String resources;

    @Column(name = "redirect_uris", length = 500)
    private String redirectUris;

    @Column(name = "locked", length = 3)
    private String locked;

    @Column(name = "enabled", length = 3)
    private String enabled;

    @ManyToMany(cascade = CascadeType.REFRESH)
    @JoinTable(name = "sys_client_role",
            joinColumns = {
                    @JoinColumn(name = "client_id", referencedColumnName = "id"),
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "role_id", referencedColumnName = "id"),
            }
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Collection<SysRole> sysRoles = new HashSet<>();
}
