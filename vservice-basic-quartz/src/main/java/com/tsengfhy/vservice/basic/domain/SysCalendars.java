package com.tsengfhy.vservice.basic.domain;

import com.tsengfhy.vservice.basic.domain.key.SysCalendarsPK;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "sys_calendars", schema = "sys")
public class SysCalendars implements Serializable {

    private static final long serialVersionUID = 839155842856082487L;

    @Id
    private SysCalendarsPK id;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "calendar", columnDefinition = "BLOB", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private byte[] calendar;
}
