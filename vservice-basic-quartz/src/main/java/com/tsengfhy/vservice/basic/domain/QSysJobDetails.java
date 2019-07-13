package com.tsengfhy.vservice.basic.domain;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.*;

import javax.annotation.Generated;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QSysJobDetails is a Querydsl query type for SysJobDetails
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSysJobDetails extends EntityPathBase<SysJobDetails> {

    private static final long serialVersionUID = -2038205394L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSysJobDetails sysJobDetails = new QSysJobDetails("sysJobDetails");

    public final StringPath description = createString("description");

    public final com.tsengfhy.vservice.basic.domain.key.QSysJobDetailsPK id;

    public final StringPath idDurable = createString("idDurable");

    public final StringPath isNonconcurrent = createString("isNonconcurrent");

    public final StringPath isUpdateData = createString("isUpdateData");

    public final StringPath jobClassName = createString("jobClassName");

    public final ArrayPath<byte[], Byte> jobData = createArray("jobData", byte[].class);

    public final StringPath requestsRecovery = createString("requestsRecovery");

    public final CollectionPath<SysTriggers, QSysTriggers> sysTriggers = this.<SysTriggers, QSysTriggers>createCollection("sysTriggers", SysTriggers.class, QSysTriggers.class, PathInits.DIRECT2);

    public QSysJobDetails(String variable) {
        this(SysJobDetails.class, forVariable(variable), INITS);
    }

    public QSysJobDetails(Path<? extends SysJobDetails> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSysJobDetails(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSysJobDetails(PathMetadata metadata, PathInits inits) {
        this(SysJobDetails.class, metadata, inits);
    }

    public QSysJobDetails(Class<? extends SysJobDetails> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.id = inits.isInitialized("id") ? new com.tsengfhy.vservice.basic.domain.key.QSysJobDetailsPK(forProperty("id")) : null;
    }

}

