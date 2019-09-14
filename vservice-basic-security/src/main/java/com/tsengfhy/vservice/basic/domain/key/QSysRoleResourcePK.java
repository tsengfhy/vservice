package com.tsengfhy.vservice.basic.domain.key;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.BeanPath;
import com.querydsl.core.types.dsl.NumberPath;

import javax.annotation.Generated;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QSysRoleResourcePK is a Querydsl query type for SysRoleResourcePK
 */
@Generated("com.querydsl.codegen.EmbeddableSerializer")
public class QSysRoleResourcePK extends BeanPath<SysRoleResourcePK> {

    private static final long serialVersionUID = 1687413689L;

    public static final QSysRoleResourcePK sysRoleResourcePK = new QSysRoleResourcePK("sysRoleResourcePK");

    public final NumberPath<Long> resourceId = createNumber("resourceId", Long.class);

    public final NumberPath<Long> roleId = createNumber("roleId", Long.class);

    public QSysRoleResourcePK(String variable) {
        super(SysRoleResourcePK.class, forVariable(variable));
    }

    public QSysRoleResourcePK(Path<? extends SysRoleResourcePK> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSysRoleResourcePK(PathMetadata metadata) {
        super(SysRoleResourcePK.class, metadata);
    }

}

