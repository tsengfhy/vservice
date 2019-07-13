package com.tsengfhy.vservice.basic.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Date;

@Data
public abstract class AbstractDto implements Serializable {

    protected static final long serialVersionUID = 0L;

    @ApiModelProperty(value = "Id", position = 1001)
    protected Long id;

    @ApiModelProperty(value = "Creator Id", position = 1002)
    protected Long createdBy;

    @ApiModelProperty(value = "Created date", position = 1003, example = "2008-01-01 00:00:00")
    protected Date createdDate;

    @ApiModelProperty(value = "Last editor Id", position = 1004)
    protected Long lastModifiedBy;

    @ApiModelProperty(value = "Last edited date", position = 1005, example = "2008-01-01 00:00:00")
    protected Date lastModifiedDate;

    @Override
    public String toString() {
        return String.format("Dto of type %s with id: %s", this.getClass().getName(), this.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof AbstractDto)) {
            return false;
        } else {
            AbstractDto that = (AbstractDto) o;
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
