package com.tsengfhy.vservice.basic.domain;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.PathInits;
import com.querydsl.core.types.dsl.StringPath;

import javax.annotation.Generated;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QSysFiredTriggers is a Querydsl query type for SysFiredTriggers
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSysFiredTriggers extends EntityPathBase<SysFiredTriggers> {

    private static final long serialVersionUID = 1368342464L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSysFiredTriggers sysFiredTriggers = new QSysFiredTriggers("sysFiredTriggers");

    public final NumberPath<Long> firedTime = createNumber("firedTime", Long.class);

    public final com.tsengfhy.vservice.basic.domain.key.QSysFiredTriggersPK id;

    public final StringPath instanceName = createString("instanceName");

    public final StringPath isNonconcurrent = createString("isNonconcurrent");

    public final StringPath jobGroup = createString("jobGroup");

    public final StringPath jobName = createString("jobName");

    public final NumberPath<Integer> priority = createNumber("priority", Integer.class);

    public final StringPath requestsRecovery = createString("requestsRecovery");

    public final NumberPath<Long> schedTime = createNumber("schedTime", Long.class);

    public final StringPath state = createString("state");

    public final StringPath triggerGroup = createString("triggerGroup");

    public final StringPath triggerName = createString("triggerName");

    public QSysFiredTriggers(String variable) {
        this(SysFiredTriggers.class, forVariable(variable), INITS);
    }

    public QSysFiredTriggers(Path<? extends SysFiredTriggers> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSysFiredTriggers(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSysFiredTriggers(PathMetadata metadata, PathInits inits) {
        this(SysFiredTriggers.class, metadata, inits);
    }

    public QSysFiredTriggers(Class<? extends SysFiredTriggers> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.id = inits.isInitialized("id") ? new com.tsengfhy.vservice.basic.domain.key.QSysFiredTriggersPK(forProperty("id")) : null;
    }

}

