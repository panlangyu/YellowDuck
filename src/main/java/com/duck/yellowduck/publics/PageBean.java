package com.duck.yellowduck.publics;

import java.util.List;

public class PageBean<T>
{
    private Integer currentPage = Integer.valueOf(1);
    private Integer currentSize = Integer.valueOf(10);
    private long totalNum;
    private List<T> items;
    private int totalPage;

    public PageBean() {}

    public PageBean(Integer currentPage, Integer currentSize, long totaNum)
    {
        this.currentPage = currentPage;
        this.currentSize = currentSize;
        this.totalNum = this.totalNum;
    }

    public Integer getCurrentPage()
    {
        return this.currentPage;
    }

    public void setCurrentPage(Integer currentPage)
    {
        this.currentPage = currentPage;
    }

    public Integer getCurrentSize()
    {
        return this.currentSize;
    }

    public void setCurrentSize(Integer currentSize)
    {
        this.currentSize = currentSize;
    }

    public long getTotalNum()
    {
        return this.totalNum;
    }

    public void setTotalNum(long totalNum)
    {
        this.totalNum = totalNum;
    }

    public List<T> getItems()
    {
        return this.items;
    }

    public void setItems(List<T> items)
    {
        this.items = items;
    }

    public int getTotalPage()
    {
        if (this.totalNum % this.currentSize.intValue() == 0L) {
            return (int)(this.totalNum / this.currentSize.intValue());
        }
        return (int)(this.totalNum / this.currentSize.intValue()) + 1;
    }

    public void setTotalPage(int totalPage)
    {
        this.totalPage = totalPage;
    }
}
