package com.tsengfhy.vservice.basic.domain;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.PathInits;

import javax.annotation.Generated;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QSysClientRole is a Querydsl query type for SysClientRole
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSysClientRole extends EntityPathBase<SysClientRole> {

    private static final long serialVersionUID = -740372662L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSysClientRole sysClientRole = new QSysClientRole("sysClientRole");

    public final com.tsengfhy.vservice.basic.domain.key.QSysClientRolePK id;

    public final QSysClient sysClient;

    public final QSysRole sysRole;

    public QSysClientRole(String variable) {
        this(SysClientRole.class, forVariable(variable), INITS);
    }

    public QSysClientRole(Path<? extends SysClientRole> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSysClientRole(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSysClientRole(PathMetadata metadata, PathInits inits) {
        this(SysClientRole.class, metadata, inits);
    }

    public QSysClientRole(Class<? extends SysClientRole> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.id = inits.isInitialized("id") ? new com.tsengfhy.vservice.basic.domain.key.QSysClientRolePK(forProperty("id")) : null;
        this.sysClient = inits.isInitialized("sysClient") ? new QSysClient(forProperty("sysClient")) : null;
        this.sysRole = inits.isInitialized("sysRole") ? new QSysRole(forProperty("sysRole"), inits.get("sysRole")) : null;
    }

}

