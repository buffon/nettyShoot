package com.baidu.harry.rpc.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created with IntelliJ IDEA.
 * User: chenyehui
 * Date: 14-11-7
 * Time: 下午1:37
 * To change this template use File | Settings | File Templates.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EndPoint {

    String value() default "";

}
