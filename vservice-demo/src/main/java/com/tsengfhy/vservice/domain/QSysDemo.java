package com.tsengfhy.vservice.domain;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import com.tsengfhy.vservice.basic.domain.QAbstractEntity;

import javax.annotation.Generated;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QSysDemo is a Querydsl query type for SysDemo
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSysDemo extends EntityPathBase<SysDemo> {

    private static final long serialVersionUID = -233173044L;

    public static final QSysDemo sysDemo = new QSysDemo("sysDemo");

    public final QAbstractEntity _super = new QAbstractEntity(this);

    public final StringPath content = createString("content");

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

    public final StringPath title = createString("title");

    public QSysDemo(String variable) {
        super(SysDemo.class, forVariable(variable));
    }

    public QSysDemo(Path<? extends SysDemo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSysDemo(PathMetadata metadata) {
        super(SysDemo.class, metadata);
    }

}

