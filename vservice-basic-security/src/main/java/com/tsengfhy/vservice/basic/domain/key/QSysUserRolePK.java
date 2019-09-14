package com.tsengfhy.vservice.basic.domain.key;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.BeanPath;
import com.querydsl.core.types.dsl.NumberPath;

import javax.annotation.Generated;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QSysUserRolePK is a Querydsl query type for SysUserRolePK
 */
@Generated("com.querydsl.codegen.EmbeddableSerializer")
public class QSysUserRolePK extends BeanPath<SysUserRolePK> {

    private static final long serialVersionUID = -1332275786L;

    public static final QSysUserRolePK sysUserRolePK = new QSysUserRolePK("sysUserRolePK");

    public final NumberPath<Long> roleId = createNumber("roleId", Long.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QSysUserRolePK(String variable) {
        super(SysUserRolePK.class, forVariable(variable));
    }

    public QSysUserRolePK(Path<? extends SysUserRolePK> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSysUserRolePK(PathMetadata metadata) {
        super(SysUserRolePK.class, metadata);
    }

}

