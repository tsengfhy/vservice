package com.tsengfhy.vservice.basic.domain;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "sys_token", schema = "sys",
        indexes = {
                @Index(name = "IDX_SYS_TOKEN_USERNAME", columnList = "username"),
        }
)
public class SysToken {

    @Id
    @Column(name = "id", length = 64)
    private String id;

    @Column(name = "username", length = 64)
    private String username;

    @Column(name = "token", length = 64)
    private String token;

    @Column(name = "last_modified_date")
    private Timestamp lastModifiedDate;
}
