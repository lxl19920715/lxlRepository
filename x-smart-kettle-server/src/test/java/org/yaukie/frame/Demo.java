package org.yaukie.frame;

import org.yaukie.frame.pool.StandardPoolExecutor;
import org.yaukie.frame.pool.StandardThreadFactory;
import org.yaukie.xtl.log.LoggingEventListener;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class Demo {
    /**通过引用来记录线程池工厂中线程*/
    private static Set<Thread> threadsContainer = new HashSet<>();

    private static Map<String,Thread> activeMap = new ConcurrentHashMap();

    public static void main(String[] args) {
        Map<String,String> map = new HashMap<String,String>();
        map.put("1", "value 1");
        map.put("2", "value 2");
        map.put("3", "value 3");
        map.put("4", "value 4");
        map.put("5", "value 5");

        map.remove("3");

//        Set<Map.Entry<String,String>> entries = map.entrySet();
//        Iterator iter = entries.iterator();
//        Object obj = null;
//        while(iter.hasNext()) {
//            Map.Entry<String,String> entry = (Map.Entry)iter.next();
//            Object key = entry.getKey();
//            obj = key;
//            System.out.println(obj);
//        }
//        for (String key : map.keySet()) {
//            System.out.println("map.get(" + key + ") = " + map.get(key));
//        }
//        map.forEach((k,v) -> System.out.println("key:value = " + k + ":" + v));
//        StandardPoolExecutor executor = new StandardPoolExecutor();
//        StandardThreadFactory threadFactory = new StandardThreadFactory("kettleThreadPool",threadsContainer);
//        threadFactory.setJobName("jobname");
//        executor.setThreadFactory(threadFactory);
//        executor.allowCoreThreadTimeOut(true);

        String a = "开始时间:2";
        String aa = "作业描述:3";
        String a2 = "作业名称:4";
        String a3 = "kjb路径:5";
        String a4 = "log:233";

        System.out.println(a+"\n" + aa + "\n" + a2 + "\n" + a3 + "\n" +a4);

    }

    public static void threadAlive(){
        AtomicBoolean isExist = new AtomicBoolean(false);
        threadsContainer.stream().filter(Thread::isAlive)
                .forEach(thread -> {
                    String threadName = thread.getName() ;
//                    log.debug("当前运行的任务：{} \t\n",threadName);
                    System.out.println("当前运行的任务：{} \t\n" + threadName);
                    if(activeMap.containsKey(threadName)){
                        isExist.set(true);
                    }
                    activeMap.put(threadName, thread);
                });
    }
}
