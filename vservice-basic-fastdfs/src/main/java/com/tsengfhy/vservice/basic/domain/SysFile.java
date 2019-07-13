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
@Table(name = "sys_file", schema = "sys",
        indexes = {
                @Index(name = "IDX_SYS_FILE_TYPE", columnList = "type"),
                @Index(name = "IDX_SYS_FILE_NAME", columnList = "name"),
                @Index(name = "IDX_SYS_FILE_FOREIGN_TYPE", columnList = "foreign_type"),
                @Index(name = "IDX_SYS_FILE_FOREIGN_ID", columnList = "foreign_id")
        }
)
public class SysFile extends AbstractEntity {

    @Column(name = "type", length = 10, nullable = false)
    private String type;

    @Column(name = "name", length = 200, nullable = false)
    private String name;

    @Column(name = "size", nullable = false)
    private Long size;

    @Column(name = "uri", length = 200, nullable = false)
    private String uri;

    @Column(name = "thumb_uri", length = 200)
    private String thumbUri;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "foreign_type", length = 100)
    private String foreignType;

    @Column(name = "foreign_id")
    private Long foreignId;

    @Column(name = "series")
    private Integer series;

    @Column(name = "completed", length = 3)
    private String completed;
}
