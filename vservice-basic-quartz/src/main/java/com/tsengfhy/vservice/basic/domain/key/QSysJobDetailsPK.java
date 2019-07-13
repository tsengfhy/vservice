package com.tsengfhy.vservice.basic.domain.key;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.BeanPath;
import com.querydsl.core.types.dsl.StringPath;

import javax.annotation.Generated;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QSysJobDetailsPK is a Querydsl query type for SysJobDetailsPK
 */
@Generated("com.querydsl.codegen.EmbeddableSerializer")
public class QSysJobDetailsPK extends BeanPath<SysJobDetailsPK> {

    private static final long serialVersionUID = 1770076954L;

    public static final QSysJobDetailsPK sysJobDetailsPK = new QSysJobDetailsPK("sysJobDetailsPK");

    public final StringPath jobGroup = createString("jobGroup");

    public final StringPath jobName = createString("jobName");

    public final StringPath schedName = createString("schedName");

    public QSysJobDetailsPK(String variable) {
        super(SysJobDetailsPK.class, forVariable(variable));
    }

    public QSysJobDetailsPK(Path<? extends SysJobDetailsPK> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSysJobDetailsPK(PathMetadata metadata) {
        super(SysJobDetailsPK.class, metadata);
    }

}

