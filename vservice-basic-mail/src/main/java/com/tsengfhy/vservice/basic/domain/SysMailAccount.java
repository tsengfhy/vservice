package com.tsengfhy.vservice.basic.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "sys_mail_account", schema = "sys",
        indexes = {
                @Index(name = "IDX_SYS_MAIL_ACCOUNT_USERNAME", columnList = "username")
        }
)
public class SysMailAccount extends AbstractEntity {

    @Column(name = "username", length = 100)
    private String username;

    @Column(name = "password", length = 100)
    private String password;
}
