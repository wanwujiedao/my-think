package com.dao.wwjd.filter.test;

import com.alibaba.fastjson.JSON;
import com.dao.wwjd.filter.core.AbstractFilter;
import com.dao.wwjd.filter.core.FilterChainFactory;
import com.dao.wwjd.filter.filter.EvenNumberFilter;
import com.dao.wwjd.filter.filter.IncludeFourFilter;
import com.dao.wwjd.filter.filter.ThreeFilter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 整型数字的责任链
 *
 * @author 阿导
 * @version 1.0
 * @fileName com.dao.wwjd.filter.test.FilterTest.java
 * @CopyRright (c) 2018-万物皆导
 * @created 2018-04-17 12:33:00
 */
public class FilterTest {
    private static FilterChainFactory filterChainFactory;
    private static List<Integer> list ;
    private static FilterChainFactory filterChainFactoryAnother;

    static {
        filterChainFactoryAnother = new FilterChainFactory();
        filterChainFactoryAnother.addFilter(new com.dao.wwjd.filter.anotherfilter.IncludeFourFilter());
        filterChainFactoryAnother.addFilter(new com.dao.wwjd.filter.anotherfilter.EvenNumberFilter());
        filterChainFactoryAnother.addFilter(new com.dao.wwjd.filter.anotherfilter.ThreeFilter());

        filterChainFactory = new FilterChainFactory();
        filterChainFactory.addFilter(new IncludeFourFilter());
        filterChainFactory.addFilter(new EvenNumberFilter());
        filterChainFactory.addFilter(new ThreeFilter());
        list = new ArrayList<>();
        for (int i = 0; i < 100000; i++) {
            list.add(i);
        }
    }

    public static void main(String[] args) {
        test();
        testList();

    }

    private static void testList(){
        Long start=System.currentTimeMillis();
        AbstractFilter filters = filterChainFactory.getFilters();
        List dealList=new ArrayList(list);
        filters.doFilter(dealList);
        System.out.println(JSON.toJSONString(dealList));
        System.out.println("集合作为处理对象:"+(System.currentTimeMillis()-start));
    }
    private static void test(){
        Long start=System.currentTimeMillis();
        ArrayList arrayList = new ArrayList(list);
        doFilter(arrayList);
        System.out.println(JSON.toJSONString(arrayList));
        System.out.println("集合里面的元素进行处理:"+(System.currentTimeMillis()-start));
    }

    private static void doFilter(List<Integer> list) {
        AbstractFilter filters = filterChainFactoryAnother.getFilters();
        Iterator<Integer> iterator = list.iterator();
        for (; iterator.hasNext(); ) {
            Integer intNum = iterator.next();
            if (!filters.doFilterHasReturn(intNum)) {
                iterator.remove();
            }
        }
    }
}
