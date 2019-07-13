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
 * QSysFile is a Querydsl query type for SysFile
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSysFile extends EntityPathBase<SysFile> {

    private static final long serialVersionUID = 1417378981L;

    public static final QSysFile sysFile = new QSysFile("sysFile");

    public final QAbstractEntity _super = new QAbstractEntity(this);

    public final StringPath completed = createString("completed");

    //inherited
    public final NumberPath<Long> createdBy = _super.createdBy;

    //inherited
    public final DateTimePath<java.util.Date> createdDate = _super.createdDate;

    public final StringPath description = createString("description");

    public final NumberPath<Long> foreignId = createNumber("foreignId", Long.class);

    public final StringPath foreignType = createString("foreignType");

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final NumberPath<Long> lastModifiedBy = _super.lastModifiedBy;

    //inherited
    public final DateTimePath<java.util.Date> lastModifiedDate = _super.lastModifiedDate;

    public final StringPath name = createString("name");

    public final NumberPath<Integer> series = createNumber("series", Integer.class);

    public final NumberPath<Long> size = createNumber("size", Long.class);

    public final StringPath thumbUri = createString("thumbUri");

    public final StringPath type = createString("type");

    public final StringPath uri = createString("uri");

    public QSysFile(String variable) {
        super(SysFile.class, forVariable(variable));
    }

    public QSysFile(Path<? extends SysFile> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSysFile(PathMetadata metadata) {
        super(SysFile.class, metadata);
    }

}

