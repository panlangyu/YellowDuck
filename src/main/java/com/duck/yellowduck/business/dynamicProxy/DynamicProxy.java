package com.duck.yellowduck.business.dynamicProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class DynamicProxy
        implements InvocationHandler
{
    Object target;

    public Object newProxyInstance(Object target)
    {
        this.target = target;
        return Proxy.newProxyInstance(target.getClass().getClassLoader(), target
                .getClass().getInterfaces(), this);
    }

    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable
    {
        return method.invoke(this.target, args);
    }
}
