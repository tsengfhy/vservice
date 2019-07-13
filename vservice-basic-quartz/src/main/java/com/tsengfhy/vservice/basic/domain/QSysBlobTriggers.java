package com.tsengfhy.vservice.basic.domain;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.ArrayPath;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.PathInits;

import javax.annotation.Generated;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QSysBlobTriggers is a Querydsl query type for SysBlobTriggers
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSysBlobTriggers extends EntityPathBase<SysBlobTriggers> {

    private static final long serialVersionUID = 2093176481L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSysBlobTriggers sysBlobTriggers = new QSysBlobTriggers("sysBlobTriggers");

    public final ArrayPath<byte[], Byte> blobData = createArray("blobData", byte[].class);

    public final QSysTriggers sysTriggers;

    public QSysBlobTriggers(String variable) {
        this(SysBlobTriggers.class, forVariable(variable), INITS);
    }

    public QSysBlobTriggers(Path<? extends SysBlobTriggers> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSysBlobTriggers(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSysBlobTriggers(PathMetadata metadata, PathInits inits) {
        this(SysBlobTriggers.class, metadata, inits);
    }

    public QSysBlobTriggers(Class<? extends SysBlobTriggers> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.sysTriggers = inits.isInitialized("sysTriggers") ? new QSysTriggers(forProperty("sysTriggers"), inits.get("sysTriggers")) : null;
    }

}

