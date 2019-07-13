package com.tsengfhy.vservice.basic.domain.key;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.BeanPath;
import com.querydsl.core.types.dsl.StringPath;

import javax.annotation.Generated;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QSysLocksPK is a Querydsl query type for SysLocksPK
 */
@Generated("com.querydsl.codegen.EmbeddableSerializer")
public class QSysLocksPK extends BeanPath<SysLocksPK> {

    private static final long serialVersionUID = 858638793L;

    public static final QSysLocksPK sysLocksPK = new QSysLocksPK("sysLocksPK");

    public final StringPath lockName = createString("lockName");

    public final StringPath schedName = createString("schedName");

    public QSysLocksPK(String variable) {
        super(SysLocksPK.class, forVariable(variable));
    }

    public QSysLocksPK(Path<? extends SysLocksPK> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSysLocksPK(PathMetadata metadata) {
        super(SysLocksPK.class, metadata);
    }

}

