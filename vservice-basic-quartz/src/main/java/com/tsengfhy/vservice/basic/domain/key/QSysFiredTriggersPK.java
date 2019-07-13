package com.tsengfhy.vservice.basic.domain.key;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.BeanPath;
import com.querydsl.core.types.dsl.StringPath;

import javax.annotation.Generated;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QSysFiredTriggersPK is a Querydsl query type for SysFiredTriggersPK
 */
@Generated("com.querydsl.codegen.EmbeddableSerializer")
public class QSysFiredTriggersPK extends BeanPath<SysFiredTriggersPK> {

    private static final long serialVersionUID = -2014468182L;

    public static final QSysFiredTriggersPK sysFiredTriggersPK = new QSysFiredTriggersPK("sysFiredTriggersPK");

    public final StringPath entryId = createString("entryId");

    public final StringPath schedName = createString("schedName");

    public QSysFiredTriggersPK(String variable) {
        super(SysFiredTriggersPK.class, forVariable(variable));
    }

    public QSysFiredTriggersPK(Path<? extends SysFiredTriggersPK> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSysFiredTriggersPK(PathMetadata metadata) {
        super(SysFiredTriggersPK.class, metadata);
    }

}

