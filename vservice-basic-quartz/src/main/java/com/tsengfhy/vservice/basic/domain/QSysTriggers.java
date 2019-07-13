package com.tsengfhy.vservice.basic.domain;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.*;

import javax.annotation.Generated;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QSysTriggers is a Querydsl query type for SysTriggers
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSysTriggers extends EntityPathBase<SysTriggers> {

    private static final long serialVersionUID = 17378404L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSysTriggers sysTriggers = new QSysTriggers("sysTriggers");

    public final StringPath calendarName = createString("calendarName");

    public final StringPath description = createString("description");

    public final NumberPath<Long> endTime = createNumber("endTime", Long.class);

    public final com.tsengfhy.vservice.basic.domain.key.QSysTriggersPK id;

    public final ArrayPath<byte[], Byte> jobData = createArray("jobData", byte[].class);

    public final NumberPath<Short> misfireInstr = createNumber("misfireInstr", Short.class);

    public final NumberPath<Long> nextFireTime = createNumber("nextFireTime", Long.class);

    public final NumberPath<Long> prevFireTime = createNumber("prevFireTime", Long.class);

    public final NumberPath<Integer> priority = createNumber("priority", Integer.class);

    public final NumberPath<Long> startTime = createNumber("startTime", Long.class);

    public final QSysBlobTriggers sysBlobTriggers;

    public final QSysCronTriggers sysCronTriggers;

    public final QSysJobDetails sysJobDetails;

    public final QSysSimpleTriggers sysSimpleTriggers;

    public final QSysSimpropTriggers sysSimpropTriggers;

    public final StringPath triggerState = createString("triggerState");

    public final StringPath triggerType = createString("triggerType");

    public QSysTriggers(String variable) {
        this(SysTriggers.class, forVariable(variable), INITS);
    }

    public QSysTriggers(Path<? extends SysTriggers> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSysTriggers(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSysTriggers(PathMetadata metadata, PathInits inits) {
        this(SysTriggers.class, metadata, inits);
    }

    public QSysTriggers(Class<? extends SysTriggers> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.id = inits.isInitialized("id") ? new com.tsengfhy.vservice.basic.domain.key.QSysTriggersPK(forProperty("id")) : null;
        this.sysBlobTriggers = inits.isInitialized("sysBlobTriggers") ? new QSysBlobTriggers(forProperty("sysBlobTriggers"), inits.get("sysBlobTriggers")) : null;
        this.sysCronTriggers = inits.isInitialized("sysCronTriggers") ? new QSysCronTriggers(forProperty("sysCronTriggers"), inits.get("sysCronTriggers")) : null;
        this.sysJobDetails = inits.isInitialized("sysJobDetails") ? new QSysJobDetails(forProperty("sysJobDetails"), inits.get("sysJobDetails")) : null;
        this.sysSimpleTriggers = inits.isInitialized("sysSimpleTriggers") ? new QSysSimpleTriggers(forProperty("sysSimpleTriggers"), inits.get("sysSimpleTriggers")) : null;
        this.sysSimpropTriggers = inits.isInitialized("sysSimpropTriggers") ? new QSysSimpropTriggers(forProperty("sysSimpropTriggers"), inits.get("sysSimpropTriggers")) : null;
    }

}

