package com.tsengfhy.vservice.basic.domain;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.PathInits;

import javax.annotation.Generated;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QSysPausedTriggerGrps is a Querydsl query type for SysPausedTriggerGrps
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSysPausedTriggerGrps extends EntityPathBase<SysPausedTriggerGrps> {

    private static final long serialVersionUID = -3144849L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSysPausedTriggerGrps sysPausedTriggerGrps = new QSysPausedTriggerGrps("sysPausedTriggerGrps");

    public final com.tsengfhy.vservice.basic.domain.key.QSysPausedTriggerGrpsPK id;

    public QSysPausedTriggerGrps(String variable) {
        this(SysPausedTriggerGrps.class, forVariable(variable), INITS);
    }

    public QSysPausedTriggerGrps(Path<? extends SysPausedTriggerGrps> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSysPausedTriggerGrps(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSysPausedTriggerGrps(PathMetadata metadata, PathInits inits) {
        this(SysPausedTriggerGrps.class, metadata, inits);
    }

    public QSysPausedTriggerGrps(Class<? extends SysPausedTriggerGrps> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.id = inits.isInitialized("id") ? new com.tsengfhy.vservice.basic.domain.key.QSysPausedTriggerGrpsPK(forProperty("id")) : null;
    }

}

