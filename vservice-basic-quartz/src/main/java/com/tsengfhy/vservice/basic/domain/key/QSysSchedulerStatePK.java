package com.tsengfhy.vservice.basic.domain.key;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.BeanPath;
import com.querydsl.core.types.dsl.StringPath;

import javax.annotation.Generated;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QSysSchedulerStatePK is a Querydsl query type for SysSchedulerStatePK
 */
@Generated("com.querydsl.codegen.EmbeddableSerializer")
public class QSysSchedulerStatePK extends BeanPath<SysSchedulerStatePK> {

    private static final long serialVersionUID = -1871966549L;

    public static final QSysSchedulerStatePK sysSchedulerStatePK = new QSysSchedulerStatePK("sysSchedulerStatePK");

    public final StringPath instanceName = createString("instanceName");

    public final StringPath schedName = createString("schedName");

    public QSysSchedulerStatePK(String variable) {
        super(SysSchedulerStatePK.class, forVariable(variable));
    }

    public QSysSchedulerStatePK(Path<? extends SysSchedulerStatePK> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSysSchedulerStatePK(PathMetadata metadata) {
        super(SysSchedulerStatePK.class, metadata);
    }

}

