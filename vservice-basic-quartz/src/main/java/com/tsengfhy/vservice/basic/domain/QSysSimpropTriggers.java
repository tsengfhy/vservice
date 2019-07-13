package com.tsengfhy.vservice.basic.domain;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.PathInits;
import com.querydsl.core.types.dsl.StringPath;

import javax.annotation.Generated;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QSysSimpropTriggers is a Querydsl query type for SysSimpropTriggers
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSysSimpropTriggers extends EntityPathBase<SysSimpropTriggers> {

    private static final long serialVersionUID = -358998580L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSysSimpropTriggers sysSimpropTriggers = new QSysSimpropTriggers("sysSimpropTriggers");

    public final StringPath boolProp1 = createString("boolProp1");

    public final StringPath boolProp2 = createString("boolProp2");

    public final NumberPath<java.math.BigDecimal> decProp1 = createNumber("decProp1", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> decProp2 = createNumber("decProp2", java.math.BigDecimal.class);

    public final NumberPath<Integer> intProp1 = createNumber("intProp1", Integer.class);

    public final NumberPath<Integer> intProp2 = createNumber("intProp2", Integer.class);

    public final NumberPath<Long> longProp1 = createNumber("longProp1", Long.class);

    public final NumberPath<Long> longProp2 = createNumber("longProp2", Long.class);

    public final StringPath strProp1 = createString("strProp1");

    public final StringPath strProp2 = createString("strProp2");

    public final StringPath strProp3 = createString("strProp3");

    public final QSysTriggers sysTriggers;

    public QSysSimpropTriggers(String variable) {
        this(SysSimpropTriggers.class, forVariable(variable), INITS);
    }

    public QSysSimpropTriggers(Path<? extends SysSimpropTriggers> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSysSimpropTriggers(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSysSimpropTriggers(PathMetadata metadata, PathInits inits) {
        this(SysSimpropTriggers.class, metadata, inits);
    }

    public QSysSimpropTriggers(Class<? extends SysSimpropTriggers> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.sysTriggers = inits.isInitialized("sysTriggers") ? new QSysTriggers(forProperty("sysTriggers"), inits.get("sysTriggers")) : null;
    }

}

