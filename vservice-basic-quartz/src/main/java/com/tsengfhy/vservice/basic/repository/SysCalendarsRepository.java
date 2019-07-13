package com.tsengfhy.vservice.basic.repository;

import com.tsengfhy.vservice.basic.domain.SysCalendars;
import com.tsengfhy.vservice.basic.domain.key.SysCalendarsPK;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SysCalendarsRepository extends AgileJpaRepository<SysCalendars, SysCalendarsPK> {

    @Query("select t from SysCalendars t where t.id.schedName = ?1 and t.id.calendarName = ?2")
    Optional<SysCalendars> findById(String schedName, String calendarName);
}
