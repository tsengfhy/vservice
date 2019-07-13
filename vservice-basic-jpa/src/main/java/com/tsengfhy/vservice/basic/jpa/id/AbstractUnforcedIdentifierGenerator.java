package com.tsengfhy.vservice.basic.jpa.id;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import javax.persistence.Id;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;

@Slf4j
public abstract class AbstractUnforcedIdentifierGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        long value = 0L;

        try {
            goTo:
            for (; true; ) {
                Class clazz = object.getClass();

                Optional optional = FieldUtils.getFieldsListWithAnnotation(clazz, Id.class).stream().findFirst();
                if (optional.isPresent()) {
                    value = (long) FieldUtils.readField((Field) optional.get(), object, true);
                    break goTo;
                }

                optional = MethodUtils.getMethodsListWithAnnotation(clazz, Id.class).stream().findFirst();
                if (optional.isPresent()) {
                    value = (long) MethodUtils.invokeMethod(object, true, ((Method) optional.get()).getName().replace("set", "get"));
                    break goTo;
                }
                break goTo;
            }
        } catch (Exception e) {
            log.warn("Get entity id encounter exception, new id will generate.");
        }

        if (value != 0L) {
            return value;
        }

        return this.generate();
    }

    protected abstract Serializable generate();
}
