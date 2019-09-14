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
@Table(name = "sys_role", schema = "sys",
        indexes = {
                @Index(name = "IDX_SYS_ROLE_NAME", columnList = "name"),
                @Index(name = "IDX_SYS_ROLE_TYPE", columnList = "type"),
        }
)
public class SysRole extends AbstractEntity {

    @Column(name = "type", length = 3)
    private String type;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "series")
    private Integer series;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "pid", referencedColumnName = "id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private SysRole parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.REFRESH)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Collection<SysRole> children = new HashSet<>();

    @ManyToMany(cascade = CascadeType.REFRESH)
    @JoinTable(name = "sys_role_resource",
            joinColumns = {
                    @JoinColumn(name = "role_id", referencedColumnName = "id"),
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "resource_id", referencedColumnName = "id"),
            }
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Collection<SysResource> sysResources = new HashSet<>();

    @ManyToMany(cascade = CascadeType.REFRESH)
    @JoinTable(name = "sys_user_role",
            joinColumns = {
                    @JoinColumn(name = "role_id", referencedColumnName = "id"),
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "user_id", referencedColumnName = "id"),
            }
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Collection<SysUser> sysUsers = new HashSet<>();

    @ManyToMany(cascade = CascadeType.REFRESH)
    @JoinTable(name = "sys_client_role",
            joinColumns = {
                    @JoinColumn(name = "role_id", referencedColumnName = "id"),
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "client_id", referencedColumnName = "id"),
            }
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Collection<SysClient> sysClients = new HashSet<>();
}
