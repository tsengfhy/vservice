package com.tsengfhy.vservice.basic.domain.key;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.BeanPath;
import com.querydsl.core.types.dsl.StringPath;

import javax.annotation.Generated;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QSysTriggersPK is a Querydsl query type for SysTriggersPK
 */
@Generated("com.querydsl.codegen.EmbeddableSerializer")
public class QSysTriggersPK extends BeanPath<SysTriggersPK> {

    private static final long serialVersionUID = -1527437808L;

    public static final QSysTriggersPK sysTriggersPK = new QSysTriggersPK("sysTriggersPK");

    public final StringPath schedName = createString("schedName");

    public final StringPath triggerGroup = createString("triggerGroup");

    public final StringPath triggerName = createString("triggerName");

    public QSysTriggersPK(String variable) {
        super(SysTriggersPK.class, forVariable(variable));
    }

    public QSysTriggersPK(Path<? extends SysTriggersPK> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSysTriggersPK(PathMetadata metadata) {
        super(SysTriggersPK.class, metadata);
    }

}

