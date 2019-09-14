package com.tsengfhy.vservice.basic.domain;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.*;

import javax.annotation.Generated;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QSysUser is a Querydsl query type for SysUser
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSysUser extends EntityPathBase<SysUser> {

    private static final long serialVersionUID = 1417835252L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSysUser sysUser = new QSysUser("sysUser");

    public final QAbstractEntity _super = new QAbstractEntity(this);

    //inherited
    public final NumberPath<Long> createdBy = _super.createdBy;

    //inherited
    public final DateTimePath<java.util.Date> createdDate = _super.createdDate;

    public final StringPath enabled = createString("enabled");

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final NumberPath<Long> lastModifiedBy = _super.lastModifiedBy;

    //inherited
    public final DateTimePath<java.util.Date> lastModifiedDate = _super.lastModifiedDate;

    public final StringPath locked = createString("locked");

    public final StringPath mail = createString("mail");

    public final StringPath phone = createString("phone");

    public final QSysGroup sysGroup;

    public final CollectionPath<SysRole, QSysRole> sysRoles = this.<SysRole, QSysRole>createCollection("sysRoles", SysRole.class, QSysRole.class, PathInits.DIRECT2);

    public final StringPath token = createString("token");

    public final StringPath username = createString("username");

    public QSysUser(String variable) {
        this(SysUser.class, forVariable(variable), INITS);
    }

    public QSysUser(Path<? extends SysUser> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSysUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSysUser(PathMetadata metadata, PathInits inits) {
        this(SysUser.class, metadata, inits);
    }

    public QSysUser(Class<? extends SysUser> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.sysGroup = inits.isInitialized("sysGroup") ? new QSysGroup(forProperty("sysGroup"), inits.get("sysGroup")) : null;
    }

}

