package uJo.decorator.typedata;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Abraham Medina Carrillo <https://github.com/medina1402>
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TypeFloat {
    float value() default 0f;
    boolean valueDefault() default false;

    Class<?> foreignKey() default void.class;

    boolean primaryKey() default false;
    boolean notNull() default false;
    boolean required() default false;
    boolean unique() default false;
}