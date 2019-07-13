package com.tsengfhy.vservice.basic.domain;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.PathInits;

import javax.annotation.Generated;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QSysSimpleTriggers is a Querydsl query type for SysSimpleTriggers
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSysSimpleTriggers extends EntityPathBase<SysSimpleTriggers> {

    private static final long serialVersionUID = -974287498L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSysSimpleTriggers sysSimpleTriggers = new QSysSimpleTriggers("sysSimpleTriggers");

    public final NumberPath<Long> repeatCount = createNumber("repeatCount", Long.class);

    public final NumberPath<Long> repeatInterval = createNumber("repeatInterval", Long.class);

    public final QSysTriggers sysTriggers;

    public final NumberPath<Long> timesTriggered = createNumber("timesTriggered", Long.class);

    public QSysSimpleTriggers(String variable) {
        this(SysSimpleTriggers.class, forVariable(variable), INITS);
    }

    public QSysSimpleTriggers(Path<? extends SysSimpleTriggers> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSysSimpleTriggers(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSysSimpleTriggers(PathMetadata metadata, PathInits inits) {
        this(SysSimpleTriggers.class, metadata, inits);
    }

    public QSysSimpleTriggers(Class<? extends SysSimpleTriggers> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.sysTriggers = inits.isInitialized("sysTriggers") ? new QSysTriggers(forProperty("sysTriggers"), inits.get("sysTriggers")) : null;
    }

}

