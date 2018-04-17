package com.dao.wwjd.filter.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 过滤器工厂类
 *
 * @author 阿导
 * @version 1.0
 * @fileName com.dao.wwjd.filter.core.FilterChainFactory.java
 * @CopyRright (c) 2018-万物皆导
 * @created 2018-04-17 11:29:00
 */
public class FilterChainFactory {

    public List<AbstractFilter> filters;
    {
        filters=new ArrayList<>();
    }
    public void addFilter(AbstractFilter filter){
        //添加过滤器，放入链路中
        if(filter!=null) {
           filters.add(filter);
        }
    }

    /**
     * 获取过滤器
     *
     * @author 阿导
     * @time 2018/4/17
     * @CopyRight 万物皆导
     * @param
     * @return com.dao.wwjd.filter.core.AbstractFilter
     */
    public AbstractFilter getFilters(){
        //声明结果
        AbstractFilter rs=null;
        //逆序
        Collections.reverse(filters);
        //遍历
        for(AbstractFilter filter: filters){
            //若未空
            if(rs==null){
                rs=filter;
            }else{
                //否则当前节点作为头结点
                filter.setNext(rs);
                rs=filter;
            }
        }
        //返回结果
        return rs;
    }

}
