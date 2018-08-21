package com.duck.yellowduck.business.dynamicProxy;

public class SingletoFactory
{
    private static class Factory
    {
        private static Object instance = null;

        private static synchronized void initInstance(Class clazz)
                throws IllegalAccessException, InstantiationException
        {
            if (null == instance) {
                instance = clazz.newInstance();
            }
        }

        private static Object getInstance(Class clazz)
                throws InstantiationException, IllegalAccessException
        {
            if (null == instance) {
                initInstance(clazz);
            }
            return instance;
        }
    }

    public static Object newInstance(Class clazz)
            throws IllegalAccessException, InstantiationException
    {
        return Factory.getInstance(clazz);
    }
}
