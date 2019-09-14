package com.tsengfhy.vservice.basic.domain;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.*;

import javax.annotation.Generated;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QSysResource is a Querydsl query type for SysResource
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSysResource extends EntityPathBase<SysResource> {

    private static final long serialVersionUID = -1826779465L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSysResource sysResource = new QSysResource("sysResource");

    public final QAbstractEntity _super = new QAbstractEntity(this);

    public final CollectionPath<SysResource, QSysResource> children = this.<SysResource, QSysResource>createCollection("children", SysResource.class, QSysResource.class, PathInits.DIRECT2);

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

    public final StringPath method = createString("method");

    public final StringPath name = createString("name");

    public final QSysResource parent;

    public final NumberPath<Integer> series = createNumber("series", Integer.class);

    public final CollectionPath<SysRole, QSysRole> sysRoles = this.<SysRole, QSysRole>createCollection("sysRoles", SysRole.class, QSysRole.class, PathInits.DIRECT2);

    public final StringPath type = createString("type");

    public final StringPath url = createString("url");

    public QSysResource(String variable) {
        this(SysResource.class, forVariable(variable), INITS);
    }

    public QSysResource(Path<? extends SysResource> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSysResource(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSysResource(PathMetadata metadata, PathInits inits) {
        this(SysResource.class, metadata, inits);
    }

    public QSysResource(Class<? extends SysResource> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.parent = inits.isInitialized("parent") ? new QSysResource(forProperty("parent"), inits.get("parent")) : null;
    }

}

