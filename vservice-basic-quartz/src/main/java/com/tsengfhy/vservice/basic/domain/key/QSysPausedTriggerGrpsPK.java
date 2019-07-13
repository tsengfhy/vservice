package com.tsengfhy.vservice.basic.domain.key;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.BeanPath;
import com.querydsl.core.types.dsl.StringPath;

import javax.annotation.Generated;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QSysPausedTriggerGrpsPK is a Querydsl query type for SysPausedTriggerGrpsPK
 */
@Generated("com.querydsl.codegen.EmbeddableSerializer")
public class QSysPausedTriggerGrpsPK extends BeanPath<SysPausedTriggerGrpsPK> {

    private static final long serialVersionUID = -1010455527L;

    public static final QSysPausedTriggerGrpsPK sysPausedTriggerGrpsPK = new QSysPausedTriggerGrpsPK("sysPausedTriggerGrpsPK");

    public final StringPath schedName = createString("schedName");

    public final StringPath triggerGroup = createString("triggerGroup");

    public QSysPausedTriggerGrpsPK(String variable) {
        super(SysPausedTriggerGrpsPK.class, forVariable(variable));
    }

    public QSysPausedTriggerGrpsPK(Path<? extends SysPausedTriggerGrpsPK> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSysPausedTriggerGrpsPK(PathMetadata metadata) {
        super(SysPausedTriggerGrpsPK.class, metadata);
    }

}

