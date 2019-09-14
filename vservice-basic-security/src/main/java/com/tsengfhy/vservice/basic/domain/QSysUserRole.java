package com.tsengfhy.vservice.basic.domain;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.PathInits;

import javax.annotation.Generated;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QSysUserRole is a Querydsl query type for SysUserRole
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSysUserRole extends EntityPathBase<SysUserRole> {

    private static final long serialVersionUID = -1752248950L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSysUserRole sysUserRole = new QSysUserRole("sysUserRole");

    public final com.tsengfhy.vservice.basic.domain.key.QSysUserRolePK id;

    public final QSysRole sysRole;

    public final QSysUser sysUser;

    public QSysUserRole(String variable) {
        this(SysUserRole.class, forVariable(variable), INITS);
    }

    public QSysUserRole(Path<? extends SysUserRole> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSysUserRole(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSysUserRole(PathMetadata metadata, PathInits inits) {
        this(SysUserRole.class, metadata, inits);
    }

    public QSysUserRole(Class<? extends SysUserRole> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.id = inits.isInitialized("id") ? new com.tsengfhy.vservice.basic.domain.key.QSysUserRolePK(forProperty("id")) : null;
        this.sysRole = inits.isInitialized("sysRole") ? new QSysRole(forProperty("sysRole"), inits.get("sysRole")) : null;
        this.sysUser = inits.isInitialized("sysUser") ? new QSysUser(forProperty("sysUser"), inits.get("sysUser")) : null;
    }

}

