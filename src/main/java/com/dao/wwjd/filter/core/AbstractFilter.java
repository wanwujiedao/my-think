package com.dao.wwjd.filter.core;

/**
 * 过滤器错误
 *
 * @author 阿导
 * @version 1.0
 * @fileName com.dao.wwjd.filter.core.AbstractFilter.java
 * @CopyRright (c) 2018-万物皆导
 * @created 2018-04-17 09:34:00
 */
public abstract class AbstractFilter<E> {

    /**
     * 下一个节点
     */
    private AbstractFilter next;

    /**
     * 此处存放一个有效的参数，默认有效
     */
    private boolean isValid;

    /**
     * 处理的对象
     *
     * @author 阿导
     * @time 2018/4/17
     * @CopyRight 万物皆导
     * @param isValid 本节点是否有效
     * @return
     */
    public AbstractFilter(boolean isValid){
        this.isValid=isValid;
    }
    /**
     * 处理的对象
     *
     * @author 阿导
     * @time 2018/4/17
     * @CopyRight 万物皆导
     * @return
     */
    public AbstractFilter(){
        this(true);
    }

    protected void setNext(AbstractFilter next) {
        this.next = next;
    }

    protected AbstractFilter getNext() {
        return next;
    }

    /**
     * 处理引用类型的对象
     *
     * @author 阿导
     * @time 2018/4/17
     * @CopyRight 万物皆导
     * @param e
     * @return void
     */
    public void doFilter(E e){
        if(!isValid){
            return;
        }
        doRealFilter(e);
        if(next==null){
            return;
        }
        next.doFilter(e);
    }

    /**
     * 根据返回值进行过滤
     *
     * @author 阿导
     * @time 2018/4/17
     * @CopyRight 万物皆导
     * @param e
     * @return boolean
     */
    public boolean doFilterHasReturn(E e){
        if(!isValid){
            return true;
        }
        //若当前条件不满足，直接返回 false
        boolean b = doRealFilterHasReturn(e);
        if(next==null||!b){
            return b;
        }

        return next.doFilterHasReturn(e);
    }

    protected abstract boolean doRealFilterHasReturn(E e);

    /**
     * 需要处理的对象，具体逻辑由子类完成
     *
     * @author 阿导
     * @time 2018/4/17
     * @CopyRight 万物皆导
     * @param e
     * @return void
     */
    protected abstract void doRealFilter(E e);

}
