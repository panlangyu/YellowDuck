package com.duck.yellowduck.business.task;

import com.duck.yellowduck.configuration.AppConfig;
import com.duck.yellowduck.domain.logs.Log;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CacheManager
{
    @Autowired
    protected AppConfig appConfig;
    private static HashMap cacheMap = new HashMap();
    private static long timeOut = 900000L;

    public boolean getSimpleFlag(String key)
    {
        try
        {
            return ((Boolean)cacheMap.get(key)).booleanValue();
        }
        catch (NullPointerException e) {}
        return false;
    }

    public long getServerStartdt(String key)
    {
        try
        {
            return ((Long)cacheMap.get(key)).longValue();
        }
        catch (Exception ex) {}
        return 0L;
    }

    public synchronized boolean setSimpleFlag(String key, boolean flag)
    {
        if ((flag) && (getSimpleFlag(key))) {
            return false;
        }
        cacheMap.put(key, Boolean.valueOf(flag));
        return true;
    }

    public synchronized boolean setSimpleFlag(String key, long serverbegrundt)
    {
        if (cacheMap.get(key) == null)
        {
            cacheMap.put(key, Long.valueOf(serverbegrundt));
            return true;
        }
        return false;
    }

    private synchronized Cache getCache(String key)
    {
        return (Cache)cacheMap.get(key);
    }

    private synchronized boolean hasCache(String key)
    {
        return cacheMap.containsKey(key);
    }

    public synchronized void clearAll()
    {
        cacheMap.clear();
    }

    public synchronized void clearAll(String type)
    {
        Iterator i = cacheMap.entrySet().iterator();
        try
        {
            while (i.hasNext())
            {
                Map.Entry entry = (Map.Entry)i.next();
                String key = (String)entry.getKey();
                if (key.startsWith(type)) {
                    clearOnly(key);
                }
            }
        }
        catch (Exception ex)
        {
            Log.i(getClass(), "", new Object[] { ex });
        }
    }

    public synchronized void clearOnly(String key)
    {
        cacheMap.remove(key);
    }

    public synchronized void putCache(String key, Cache obj)
    {
        cacheMap.put(key, obj);
    }

    public Cache getCacheInfo(String key)
    {
        Cache cache = null;
        if (hasCache(key))
        {
            cache = getCache(key);
            if (cacheExpired(cache))
            {
                cache.setExpired(true);
                clearOnly(key);
                cache = null;
            }
            else
            {
                cache = getCache(key);
            }
        }
        else
        {
            Log.i(CacheManager.class, "getCacheInfo", new Object[] { "��������������" });
        }
        return cache;
    }

    public void putCacheInfo(String key, Object obj, long dt, boolean expired)
    {
        Cache cache = new Cache();
        cache.setKey(key);
        cache.setTimeOut(dt + System.currentTimeMillis());
        cache.setValue(obj);
        cache.setExpired(expired);
        cacheMap.put(key, cache);
    }

    public void putCacheInfo(String key, Object obj, long dt)
    {
        Cache cache = new Cache();
        cache.setKey(key);
        cache.setTimeOut(dt + System.currentTimeMillis());
        cache.setValue(obj);
        cache.setExpired(false);
        cacheMap.put(key, cache);
    }

    public boolean cacheExpired(Cache cache)
    {
        if (null == cache) {
            return false;
        }
        long nowDt = System.currentTimeMillis();
        long cacheDt = cache.getTimeOut();
        if ((cacheDt <= 0L) || (cacheDt > nowDt)) {
            return false;
        }
        Log.i(CacheManager.class, "cacheExpired", new Object[] { "��������" });
        return true;
    }

    public int getCacheSize()
    {
        return cacheMap.size();
    }

    public int getCacheSize(String type)
    {
        int k = 0;
        Iterator i = cacheMap.entrySet().iterator();
        try
        {
            while (i.hasNext())
            {
                Map.Entry entry = (Map.Entry)i.next();
                String key = (String)entry.getKey();
                if (key.indexOf(type) != -1) {
                    k++;
                }
            }
        }
        catch (Exception ex)
        {
            Log.i(getClass(), "getCacheSize", new Object[] { ex });
        }
        return k;
    }

    public ArrayList getCacheAllkey()
    {
        ArrayList a = new ArrayList();
        try
        {
            Iterator i = cacheMap.entrySet().iterator();
            while (i.hasNext())
            {
                Map.Entry entry = (Map.Entry)i.next();
                a.add(entry.getKey());
            }
        }
        catch (Exception ex)
        {
            Log.i(getClass(), "", new Object[] { ex });
        }
        return a;
    }

    public ArrayList getCacheListkey(String type)
    {
        ArrayList a = new ArrayList();
        try
        {
            Iterator i = cacheMap.entrySet().iterator();
            while (i.hasNext())
            {
                Map.Entry entry = (Map.Entry)i.next();
                String key = (String)entry.getKey();
                if (key.indexOf(type) != -1) {
                    a.add(key);
                }
            }
        }
        catch (Exception ex)
        {
            Log.i(getClass(), "", new Object[] { ex });
        }
        return a;
    }

    public long getTimeOut()
    {
        return timeOut;
    }

    public void setTimeOut(long timeOut)
    {
        timeOut = timeOut;
    }
}
