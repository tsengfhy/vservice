package com.tsengfhy.vservice.basic.domain;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.ArrayPath;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.PathInits;

import javax.annotation.Generated;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QSysCalendars is a Querydsl query type for SysCalendars
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSysCalendars extends EntityPathBase<SysCalendars> {

    private static final long serialVersionUID = -45615252L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSysCalendars sysCalendars = new QSysCalendars("sysCalendars");

    public final ArrayPath<byte[], Byte> calendar = createArray("calendar", byte[].class);

    public final com.tsengfhy.vservice.basic.domain.key.QSysCalendarsPK id;

    public QSysCalendars(String variable) {
        this(SysCalendars.class, forVariable(variable), INITS);
    }

    public QSysCalendars(Path<? extends SysCalendars> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSysCalendars(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSysCalendars(PathMetadata metadata, PathInits inits) {
        this(SysCalendars.class, metadata, inits);
    }

    public QSysCalendars(Class<? extends SysCalendars> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.id = inits.isInitialized("id") ? new com.tsengfhy.vservice.basic.domain.key.QSysCalendarsPK(forProperty("id")) : null;
    }

}

