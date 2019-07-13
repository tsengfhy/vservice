package com.tsengfhy.vservice.basic.domain.key;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.BeanPath;
import com.querydsl.core.types.dsl.StringPath;

import javax.annotation.Generated;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QSysCalendarsPK is a Querydsl query type for SysCalendarsPK
 */
@Generated("com.querydsl.codegen.EmbeddableSerializer")
public class QSysCalendarsPK extends BeanPath<SysCalendarsPK> {

    private static final long serialVersionUID = 978416598L;

    public static final QSysCalendarsPK sysCalendarsPK = new QSysCalendarsPK("sysCalendarsPK");

    public final StringPath calendarName = createString("calendarName");

    public final StringPath schedName = createString("schedName");

    public QSysCalendarsPK(String variable) {
        super(SysCalendarsPK.class, forVariable(variable));
    }

    public QSysCalendarsPK(Path<? extends SysCalendarsPK> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSysCalendarsPK(PathMetadata metadata) {
        super(SysCalendarsPK.class, metadata);
    }

}

