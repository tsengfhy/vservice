package com.tsengfhy.vservice.basic.domain.key;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.BeanPath;
import com.querydsl.core.types.dsl.NumberPath;

import javax.annotation.Generated;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QSysClientRolePK is a Querydsl query type for SysClientRolePK
 */
@Generated("com.querydsl.codegen.EmbeddableSerializer")
public class QSysClientRolePK extends BeanPath<SysClientRolePK> {

    private static final long serialVersionUID = -848150730L;

    public static final QSysClientRolePK sysClientRolePK = new QSysClientRolePK("sysClientRolePK");

    public final NumberPath<Long> clientId = createNumber("clientId", Long.class);

    public final NumberPath<Long> roleId = createNumber("roleId", Long.class);

    public QSysClientRolePK(String variable) {
        super(SysClientRolePK.class, forVariable(variable));
    }

    public QSysClientRolePK(Path<? extends SysClientRolePK> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSysClientRolePK(PathMetadata metadata) {
        super(SysClientRolePK.class, metadata);
    }

}

