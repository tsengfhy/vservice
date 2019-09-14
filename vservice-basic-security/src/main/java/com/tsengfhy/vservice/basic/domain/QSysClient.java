package com.tsengfhy.vservice.basic.domain;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.*;

import javax.annotation.Generated;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QSysClient is a Querydsl query type for SysClient
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSysClient extends EntityPathBase<SysClient> {

    private static final long serialVersionUID = 513365172L;

    public static final QSysClient sysClient = new QSysClient("sysClient");

    public final QAbstractEntity _super = new QAbstractEntity(this);

    public final StringPath approveScopes = createString("approveScopes");

    //inherited
    public final NumberPath<Long> createdBy = _super.createdBy;

    //inherited
    public final DateTimePath<java.util.Date> createdDate = _super.createdDate;

    public final StringPath description = createString("description");

    public final StringPath enabled = createString("enabled");

    public final StringPath grantTypes = createString("grantTypes");

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final NumberPath<Long> lastModifiedBy = _super.lastModifiedBy;

    //inherited
    public final DateTimePath<java.util.Date> lastModifiedDate = _super.lastModifiedDate;

    public final StringPath locked = createString("locked");

    public final StringPath name = createString("name");

    public final StringPath redirectUris = createString("redirectUris");

    public final StringPath resources = createString("resources");

    public final StringPath scopes = createString("scopes");

    public final StringPath secret = createString("secret");

    public final CollectionPath<SysRole, QSysRole> sysRoles = this.<SysRole, QSysRole>createCollection("sysRoles", SysRole.class, QSysRole.class, PathInits.DIRECT2);

    public QSysClient(String variable) {
        super(SysClient.class, forVariable(variable));
    }

    public QSysClient(Path<? extends SysClient> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSysClient(PathMetadata metadata) {
        super(SysClient.class, metadata);
    }

}

