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
@Table(name = "sys_user", schema = "sys",
        indexes = {
                @Index(name = "IDX_SYS_USER_USERNAME", columnList = "username"),
                @Index(name = "IDX_SYS_USER_MAIL", columnList = "mail"),
                @Index(name = "IDX_SYS_USER_PHONE", columnList = "phone"),
                @Index(name = "IDX_SYS_USER_TOKEN", columnList = "token"),
        }
)
public class SysUser extends AbstractEntity {

    @Column(name = "username", length = 100)
    private String username;

    @Column(name = "mail", length = 100)
    private String mail;

    @Column(name = "phone", length = 100)
    private String phone;

    @Column(name = "token", length = 100)
    private String token;

    @Column(name = "password", length = 100)
    private String password;

    @Column(name = "locked", length = 3)
    private String locked;

    @Column(name = "enabled", length = 3)
    private String enabled;

    @ManyToMany(cascade = CascadeType.REFRESH)
    @JoinTable(name = "sys_user_role",
            joinColumns = {
                    @JoinColumn(name = "user_id", referencedColumnName = "id"),
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "role_id", referencedColumnName = "id"),
            }
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Collection<SysRole> sysRoles = new HashSet<>();

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private SysGroup sysGroup;
}
