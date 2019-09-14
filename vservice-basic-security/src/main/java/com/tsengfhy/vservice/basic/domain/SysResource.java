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
@Table(name = "sys_resource", schema = "sys",
        indexes = {
                @Index(name = "IDX_SYS_RESOURCE_NAME", columnList = "name"),
                @Index(name = "IDX_SYS_RESOURCE_TYPE", columnList = "type"),
                @Index(name = "IDX_SYS_RESOURCE_URL", columnList = "url"),
        }
)
public class SysResource extends AbstractEntity {

    @Column(name = "type", length = 3)
    private String type;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "url", length = 100)
    private String url;

    @Column(name = "method", length = 10)
    private String method;

    @Column(name = "series")
    private Integer series;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "pid", referencedColumnName = "id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private SysResource parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.REFRESH)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Collection<SysResource> children = new HashSet<>();

    @ManyToMany(cascade = CascadeType.REFRESH)
    @JoinTable(name = "sys_role_resource",
            joinColumns = {
                    @JoinColumn(name = "resource_id", referencedColumnName = "id"),
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "role_id", referencedColumnName = "id"),
            }
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Collection<SysRole> sysRoles = new HashSet<>();
}
