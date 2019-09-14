package com.tsengfhy.vservice.basic.domain;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.PathInits;

import javax.annotation.Generated;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QSysRoleResource is a Querydsl query type for SysRoleResource
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSysRoleResource extends EntityPathBase<SysRoleResource> {

    private static final long serialVersionUID = 680599757L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSysRoleResource sysRoleResource = new QSysRoleResource("sysRoleResource");

    public final com.tsengfhy.vservice.basic.domain.key.QSysRoleResourcePK id;

    public final QSysResource sysResource;

    public final QSysRole sysRole;

    public QSysRoleResource(String variable) {
        this(SysRoleResource.class, forVariable(variable), INITS);
    }

    public QSysRoleResource(Path<? extends SysRoleResource> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSysRoleResource(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSysRoleResource(PathMetadata metadata, PathInits inits) {
        this(SysRoleResource.class, metadata, inits);
    }

    public QSysRoleResource(Class<? extends SysRoleResource> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.id = inits.isInitialized("id") ? new com.tsengfhy.vservice.basic.domain.key.QSysRoleResourcePK(forProperty("id")) : null;
        this.sysResource = inits.isInitialized("sysResource") ? new QSysResource(forProperty("sysResource"), inits.get("sysResource")) : null;
        this.sysRole = inits.isInitialized("sysRole") ? new QSysRole(forProperty("sysRole"), inits.get("sysRole")) : null;
    }

}

