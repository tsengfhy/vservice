package com.tsengfhy.vservice.basic.domain;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.StringPath;

import javax.annotation.Generated;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QSysToken is a Querydsl query type for SysToken
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSysToken extends EntityPathBase<SysToken> {

    private static final long serialVersionUID = 1002182640L;

    public static final QSysToken sysToken = new QSysToken("sysToken");

    public final StringPath id = createString("id");

    public final DateTimePath<java.sql.Timestamp> lastModifiedDate = createDateTime("lastModifiedDate", java.sql.Timestamp.class);

    public final StringPath token = createString("token");

    public final StringPath username = createString("username");

    public QSysToken(String variable) {
        super(SysToken.class, forVariable(variable));
    }

    public QSysToken(Path<? extends SysToken> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSysToken(PathMetadata metadata) {
        super(SysToken.class, metadata);
    }

}

