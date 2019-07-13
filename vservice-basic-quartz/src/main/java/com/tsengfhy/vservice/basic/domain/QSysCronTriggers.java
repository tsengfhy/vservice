package com.tsengfhy.vservice.basic.domain;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.PathInits;
import com.querydsl.core.types.dsl.StringPath;

import javax.annotation.Generated;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QSysCronTriggers is a Querydsl query type for SysCronTriggers
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSysCronTriggers extends EntityPathBase<SysCronTriggers> {

    private static final long serialVersionUID = -184131438L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSysCronTriggers sysCronTriggers = new QSysCronTriggers("sysCronTriggers");

    public final StringPath cronExpression = createString("cronExpression");

    public final QSysTriggers sysTriggers;

    public final StringPath timeZoneId = createString("timeZoneId");

    public QSysCronTriggers(String variable) {
        this(SysCronTriggers.class, forVariable(variable), INITS);
    }

    public QSysCronTriggers(Path<? extends SysCronTriggers> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSysCronTriggers(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSysCronTriggers(PathMetadata metadata, PathInits inits) {
        this(SysCronTriggers.class, metadata, inits);
    }

    public QSysCronTriggers(Class<? extends SysCronTriggers> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.sysTriggers = inits.isInitialized("sysTriggers") ? new QSysTriggers(forProperty("sysTriggers"), inits.get("sysTriggers")) : null;
    }

}

