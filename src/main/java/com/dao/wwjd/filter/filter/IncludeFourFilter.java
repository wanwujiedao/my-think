package com.dao.wwjd.filter.filter;

import com.dao.wwjd.filter.core.AbstractFilter;

import java.util.Iterator;
import java.util.List;

/**
 * 不包含 4
 *
 * @author 阿导
 * @version 1.0
 * @fileName com.dao.wwjd.filter.test.IncludeFourFilter.java
 * @CopyRright (c) 2018-万物皆导
 * @created 2018-04-17 12:46:00
 */
public class IncludeFourFilter extends AbstractFilter<List<Integer>> {
    /**
     * 处理的对象
     *
     * @param isValid 本节点是否有效
     * @return
     * @author 阿导
     * @time 2018/4/17
     * @CopyRight 万物皆导
     */
    public IncludeFourFilter(boolean isValid) {
        super(isValid);
    }

    /**
     * 处理的对象
     *
     * @return
     * @author 阿导
     * @time 2018/4/17
     * @CopyRight 万物皆导
     */
    public IncludeFourFilter() {
    }

    @Override
    protected boolean doRealFilterHasReturn(List<Integer> integers) {
        return false;
    }

    /**
     * 需要处理的对象，具体逻辑由子类完成
     *
     * @param integers
     * @return void
     * @author 阿导
     * @time 2018/4/17
     * @CopyRight 万物皆导
     */
    @Override
    protected void doRealFilter(List<Integer> integers) {
        Iterator<Integer> iterator = integers.iterator();
        for(;iterator.hasNext();){
            Integer intNum=iterator.next();
            if(intNum.toString().indexOf("4")==-1){
                iterator.remove();
            }
        }
    }
}
