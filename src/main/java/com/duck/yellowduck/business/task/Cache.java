package com.duck.yellowduck.business.task;

public class Cache
{
    private String key;
    private Object value;
    private long timeOut;
    private boolean expired;

    public Cache() {}

    public Cache(String key, Object value, long timeOut, boolean expired)
    {
        this.key = key;
        this.value = value;
        this.timeOut = timeOut;
        this.expired = expired;
    }

    public String getKey()
    {
        return this.key;
    }

    public long getTimeOut()
    {
        return this.timeOut;
    }

    public Object getValue()
    {
        return this.value;
    }

    public void setKey(String string)
    {
        this.key = string;
    }

    public void setTimeOut(long l)
    {
        this.timeOut = l;
    }

    public void setValue(Object object)
    {
        this.value = object;
    }

    public boolean isExpired()
    {
        return this.expired;
    }

    public void setExpired(boolean b)
    {
        this.expired = b;
    }

    public String toString()
    {
        return "Cache{key='" + this.key + '\'' + ", value=" + this.value + ", timeOut=" + this.timeOut + ", expired=" + this.expired + '}';
    }
}
