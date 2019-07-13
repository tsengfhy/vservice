package com.tsengfhy.vservice.basic.domain.key;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class SysCalendarsPK implements Serializable {

    private static final long serialVersionUID = -4458721533837473496L;

    @Column(name = "sched_name", length = 120, nullable = false)
    private String schedName;

    @Column(name = "calendar_name", length = 190, nullable = false)
    private String calendarName;
}
