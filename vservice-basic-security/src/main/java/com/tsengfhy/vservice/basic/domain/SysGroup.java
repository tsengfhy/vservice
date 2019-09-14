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
@Table(name = "sys_group", schema = "sys",
        indexes = {
                @Index(name = "IDX_SYS_GROUP_NAME", columnList = "name"),
                @Index(name = "IDX_SYS_GROUP_TYPE", columnList = "type"),
        }
)
public class SysGroup extends AbstractEntity {

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
    private SysGroup parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.REFRESH)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Collection<SysGroup> children = new HashSet<>();

    @OneToMany(mappedBy = "sysGroup", cascade = CascadeType.REFRESH)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Collection<SysUser> sysUsers = new HashSet<>();
}
