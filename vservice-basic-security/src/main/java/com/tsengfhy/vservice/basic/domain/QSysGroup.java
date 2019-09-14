package com.tsengfhy.vservice.basic.domain;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.*;

import javax.annotation.Generated;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QSysGroup is a Querydsl query type for SysGroup
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSysGroup extends EntityPathBase<SysGroup> {

    private static final long serialVersionUID = 990270582L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSysGroup sysGroup = new QSysGroup("sysGroup");

    public final QAbstractEntity _super = new QAbstractEntity(this);

    public final CollectionPath<SysGroup, QSysGroup> children = this.<SysGroup, QSysGroup>createCollection("children", SysGroup.class, QSysGroup.class, PathInits.DIRECT2);

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

    public final QSysGroup parent;

    public final NumberPath<Integer> series = createNumber("series", Integer.class);

    public final CollectionPath<SysUser, QSysUser> sysUsers = this.<SysUser, QSysUser>createCollection("sysUsers", SysUser.class, QSysUser.class, PathInits.DIRECT2);

    public final StringPath type = createString("type");

    public QSysGroup(String variable) {
        this(SysGroup.class, forVariable(variable), INITS);
    }

    public QSysGroup(Path<? extends SysGroup> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSysGroup(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSysGroup(PathMetadata metadata, PathInits inits) {
        this(SysGroup.class, metadata, inits);
    }

    public QSysGroup(Class<? extends SysGroup> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.parent = inits.isInitialized("parent") ? new QSysGroup(forProperty("parent"), inits.get("parent")) : null;
    }

}

