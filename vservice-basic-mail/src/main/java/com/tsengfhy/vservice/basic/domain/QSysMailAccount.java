package com.tsengfhy.vservice.basic.domain;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;

import javax.annotation.Generated;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QSysMailAccount is a Querydsl query type for SysMailAccount
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSysMailAccount extends EntityPathBase<SysMailAccount> {

    private static final long serialVersionUID = 1751079213L;

    public static final QSysMailAccount sysMailAccount = new QSysMailAccount("sysMailAccount");

    public final QAbstractEntity _super = new QAbstractEntity(this);

    //inherited
    public final NumberPath<Long> createdBy = _super.createdBy;

    //inherited
    public final DateTimePath<java.util.Date> createdDate = _super.createdDate;

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final NumberPath<Long> lastModifiedBy = _super.lastModifiedBy;

    //inherited
    public final DateTimePath<java.util.Date> lastModifiedDate = _super.lastModifiedDate;

    public final StringPath password = createString("password");

    public final StringPath username = createString("username");

    public QSysMailAccount(String variable) {
        super(SysMailAccount.class, forVariable(variable));
    }

    public QSysMailAccount(Path<? extends SysMailAccount> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSysMailAccount(PathMetadata metadata) {
        super(SysMailAccount.class, metadata);
    }

}

