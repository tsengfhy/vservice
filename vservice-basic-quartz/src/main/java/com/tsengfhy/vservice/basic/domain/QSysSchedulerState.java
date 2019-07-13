package com.tsengfhy.vservice.basic.domain;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.PathInits;

import javax.annotation.Generated;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QSysSchedulerState is a Querydsl query type for SysSchedulerState
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSysSchedulerState extends EntityPathBase<SysSchedulerState> {

    private static final long serialVersionUID = 1614657663L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSysSchedulerState sysSchedulerState = new QSysSchedulerState("sysSchedulerState");

    public final NumberPath<Long> checkinInterval = createNumber("checkinInterval", Long.class);

    public final com.tsengfhy.vservice.basic.domain.key.QSysSchedulerStatePK id;

    public final NumberPath<Long> lastCheckinTime = createNumber("lastCheckinTime", Long.class);

    public QSysSchedulerState(String variable) {
        this(SysSchedulerState.class, forVariable(variable), INITS);
    }

    public QSysSchedulerState(Path<? extends SysSchedulerState> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSysSchedulerState(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSysSchedulerState(PathMetadata metadata, PathInits inits) {
        this(SysSchedulerState.class, metadata, inits);
    }

    public QSysSchedulerState(Class<? extends SysSchedulerState> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.id = inits.isInitialized("id") ? new com.tsengfhy.vservice.basic.domain.key.QSysSchedulerStatePK(forProperty("id")) : null;
    }

}

