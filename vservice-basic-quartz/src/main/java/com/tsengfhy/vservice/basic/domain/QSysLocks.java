package com.tsengfhy.vservice.basic.domain;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.PathInits;

import javax.annotation.Generated;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QSysLocks is a Querydsl query type for SysLocks
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSysLocks extends EntityPathBase<SysLocks> {

    private static final long serialVersionUID = 994786975L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSysLocks sysLocks = new QSysLocks("sysLocks");

    public final com.tsengfhy.vservice.basic.domain.key.QSysLocksPK id;

    public QSysLocks(String variable) {
        this(SysLocks.class, forVariable(variable), INITS);
    }

    public QSysLocks(Path<? extends SysLocks> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSysLocks(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSysLocks(PathMetadata metadata, PathInits inits) {
        this(SysLocks.class, metadata, inits);
    }

    public QSysLocks(Class<? extends SysLocks> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.id = inits.isInitialized("id") ? new com.tsengfhy.vservice.basic.domain.key.QSysLocksPK(forProperty("id")) : null;
    }

}

