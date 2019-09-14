package com.tsengfhy.vservice.basic.domain;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.*;

import javax.annotation.Generated;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QSysRole is a Querydsl query type for SysRole
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSysRole extends EntityPathBase<SysRole> {

    private static final long serialVersionUID = 1417742239L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSysRole sysRole = new QSysRole("sysRole");

    public final QAbstractEntity _super = new QAbstractEntity(this);

    public final CollectionPath<SysRole, QSysRole> children = this.<SysRole, QSysRole>createCollection("children", SysRole.class, QSysRole.class, PathInits.DIRECT2);

    //inherited
    public final NumberPath<Long> createdBy = _super.createdBy;

    //inherited
    public final DateTimePath<java.util.Date> createdDate = _super.createdDate;

    public final StringPath description = createString("description");

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final NumberPath<Long> lastModifiedBy = _super.lastModifiedBy;

    //inherited
    public final DateTimePath<java.util.Date> lastModifiedDate = _super.lastModifiedDate;

    public final StringPath name = createString("name");

    public final QSysRole parent;

    public final NumberPath<Integer> series = createNumber("series", Integer.class);

    public final CollectionPath<SysClient, QSysClient> sysClients = this.<SysClient, QSysClient>createCollection("sysClients", SysClient.class, QSysClient.class, PathInits.DIRECT2);

    public final CollectionPath<SysResource, QSysResource> sysResources = this.<SysResource, QSysResource>createCollection("sysResources", SysResource.class, QSysResource.class, PathInits.DIRECT2);

    public final CollectionPath<SysUser, QSysUser> sysUsers = this.<SysUser, QSysUser>createCollection("sysUsers", SysUser.class, QSysUser.class, PathInits.DIRECT2);

    public final StringPath type = createString("type");

    public QSysRole(String variable) {
        this(SysRole.class, forVariable(variable), INITS);
    }

    public QSysRole(Path<? extends SysRole> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSysRole(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSysRole(PathMetadata metadata, PathInits inits) {
        this(SysRole.class, metadata, inits);
    }

    public QSysRole(Class<? extends SysRole> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.parent = inits.isInitialized("parent") ? new QSysRole(forProperty("parent"), inits.get("parent")) : null;
    }

}

