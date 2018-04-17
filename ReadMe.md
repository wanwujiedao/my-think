# 责任链问题

***

## 误区：遍历次数越少，速度越快？

###### 也许大家觉着遍历的次数越少，速度越快，这是很多程序员的一个误区，程序运行的速度取决于空间复杂度和时间复杂度，若遍历过程中什么事都不干，那无疑遍历次数越少，速度越快，

###### 下面我来举一个例子

###### 处理集合里面的数据，数字必须为偶数，能被3整除，还必须包含4

> 抽象类过滤器祖宗，包含两种方式的过滤

**AbstractFilter.java**

```markdown

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



```

> 过滤器工厂，责任链模式

**FilterChainFactory.java**

```markdown
    
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



```


> 每次过滤对集合进行处理

**ThreeFilter.java**

```markdown

    package com.dao.wwjd.filter.filter;
    
    import com.dao.wwjd.filter.core.AbstractFilter;
    
    import java.util.Iterator;
    import java.util.List;
    
    /**
     * 不能被三整除的
     *
     * @author 阿导
     * @version 1.0
     * @fileName com.dao.wwjd.filter.test.ThreeFilter.java
     * @CopyRright (c) 2018-万物皆导
     * @created 2018-04-17 12:45:00
     */
    public class ThreeFilter extends AbstractFilter<List<Integer>> {
    
        /**
         * 处理的对象
         *
         * @param isValid 本节点是否有效
         * @return
         * @author 阿导
         * @time 2018/4/17
         * @CopyRight 万物皆导
         */
        public ThreeFilter(boolean isValid) {
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
        public ThreeFilter() {
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
                if(intNum%3!=0){
                    iterator.remove();
                }
            }
        }
    }



    
```

**IncludeFourFilter.java**

```markdown

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




```

**EvenNumberFilter.java**

```markdown

    package com.dao.wwjd.filter.filter;
    
    import com.dao.wwjd.filter.core.AbstractFilter;
    
    import java.util.Iterator;
    import java.util.List;
    
    /**
     * 偶数
     *
     * @author 阿导
     * @version 1.0
     * @fileName com.dao.wwjd.filter.test.EvenNumberFilter.java
     * @CopyRright (c) 2018-万物皆导
     * @created 2018-04-17 12:35:00
     */
    public class EvenNumberFilter extends AbstractFilter<List<Integer>> {
    
        /**
         * 处理的对象
         *
         * @param isValid 本节点是否有效
         * @return
         * @author 阿导
         * @time 2018/4/17
         * @CopyRight 万物皆导
         */
        public EvenNumberFilter(boolean isValid) {
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
        public EvenNumberFilter() {
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
                if(intNum%2!=0){
                    iterator.remove();
                }
            }
        }
    }
    
    

```

> 处理集合里面元素的过滤器

**ThreeFilter.java**

```markdown
    
    package com.dao.wwjd.filter.anotherfilter;
    
    import com.dao.wwjd.filter.core.AbstractFilter;
    
    /**
     * 不能被三整除的
     *
     * @author 阿导
     * @version 1.0
     * @fileName com.dao.wwjd.filter.test.ThreeFilter.java
     * @CopyRright (c) 2018-万物皆导
     * @created 2018-04-17 12:45:00
     */
    public class ThreeFilter extends AbstractFilter<Integer> {
    
        /**
         * 处理的对象
         *
         * @param isValid 本节点是否有效
         * @return
         * @author 阿导
         * @time 2018/4/17
         * @CopyRight 万物皆导
         */
        public ThreeFilter(boolean isValid) {
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
        public ThreeFilter() {
        }
    
        @Override
        protected boolean doRealFilterHasReturn(Integer integers) {
            return integers%3==0;
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
        protected void doRealFilter(Integer integers) {
    
        }
    }



```

**IncludeFourFilter.java**

```markdown
    
    package com.dao.wwjd.filter.anotherfilter;
    
    import com.dao.wwjd.filter.core.AbstractFilter;
    
    /**
     * 不包含 4
     *
     * @author 阿导
     * @version 1.0
     * @fileName com.dao.wwjd.filter.test.IncludeFourFilter.java
     * @CopyRright (c) 2018-万物皆导
     * @created 2018-04-17 12:46:00
     */
    public class IncludeFourFilter extends AbstractFilter<Integer> {
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
        protected boolean doRealFilterHasReturn(Integer integers) {
            return integers.toString().indexOf("4")!=-1;
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
        protected void doRealFilter(Integer integers) {
    
        }
    }



```

**EvenNumberFilter.java**

```markdown

    package com.dao.wwjd.filter.anotherfilter;
    
    import com.dao.wwjd.filter.core.AbstractFilter;
    
    /**
     * 偶数
     *
     * @author 阿导
     * @version 1.0
     * @fileName com.dao.wwjd.filter.test.EvenNumberFilter.java
     * @CopyRright (c) 2018-万物皆导
     * @created 2018-04-17 12:35:00
     */
    public class EvenNumberFilter extends AbstractFilter<Integer> {
    
        /**
         * 处理的对象
         *
         * @param isValid 本节点是否有效
         * @return
         * @author 阿导
         * @time 2018/4/17
         * @CopyRight 万物皆导
         */
        public EvenNumberFilter(boolean isValid) {
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
        public EvenNumberFilter() {
        }
    
        @Override
        protected boolean doRealFilterHasReturn(Integer integers) {
            return integers%2==0;
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
        protected void doRealFilter(Integer integers) {
    
        }
    }


```


> 演示类

```markdown

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
    
            //testList();
            test();
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


```

> 输出结果（注意，尽量保持单独启动，因为涉及到类的加载机制，导致第二次过滤一般比较快）

- 单独运行

```markdown
    
    集合里面的元素进行处理:1595
    
    集合作为处理对象:1182
    
    
```

- 集合里面元素进行处理挡在前面

```markdown
    
    集合里面的元素进行处理:2030
    集合作为处理对象:798
    
    
```

- 集合里面元素进行处理挡在后面

```markdown
    
    集合作为处理对象:1622
    集合里面的元素进行处理:1080
    

```

###### 由此可见，本次案例，单独运行，集合作为处理对象，明显速度比较快点，原因在哪？其实很简单，从复杂度分析：集合作为处理对象，每次集合都可能会浓缩，从而减少逻辑判断，但处理元素里面的元素，只有遇到 false 才会返回，因此，遍历次数越少，运行速度越快是伪命题，具体还得看遍历的时候究竟干了什么，因此在业务逻辑比较复杂，建议使用集合作为处理对象。
