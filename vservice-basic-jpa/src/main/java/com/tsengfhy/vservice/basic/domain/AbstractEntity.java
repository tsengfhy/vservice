package com.tsengfhy.vservice.basic.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractEntity implements Persistable<Long>, Serializable {

    protected static final long serialVersionUID = 0L;

    @Id
    @GeneratedValue(generator = "snowflakeId")
    @GenericGenerator(name = "snowflakeId", strategy = "com.tsengfhy.vservice.basic.jpa.id.SnowflakeGenerator")
    @Column(name = "id")
    protected Long id;

    @CreatedBy
    @Column(name = "created_by")
    protected Long createdBy;

    @CreatedDate
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    protected Date createdDate;

    @LastModifiedBy
    @Column(name = "last_modified_by")
    protected Long lastModifiedBy;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    @Temporal(TemporalType.TIMESTAMP)
    protected Date lastModifiedDate;

    @Transient
    @Override
    public boolean isNew() {
        return null == this.getId();
    }

    @Override
    public String toString() {
        return String.format("Entity of type %s with id: %s", this.getClass().getName(), this.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof AbstractEntity)) {
            return false;
        } else {
            AbstractEntity that = (AbstractEntity) o;
            return new EqualsBuilder().append(id, that.getId()).isEquals();
        }
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(1, 59)
                .append(id)
                .hashCode();
    }
}
