package com.tsengfhy.vservice.domain;

import com.tsengfhy.vservice.basic.domain.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "sys_demo")
@RedisHash("sys_demo")
@Document(indexName = "sys_demo")
public class SysDemo extends AbstractEntity {

    private String title;

    private String content;
}
