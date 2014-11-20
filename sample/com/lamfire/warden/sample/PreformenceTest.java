package com.lamfire.warden.sample;

import com.lamfire.utils.HttpClient;
import com.lamfire.utils.RandomUtils;
import com.lamfire.utils.Threads;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-11-20
 * Time: 上午9:51
 * To change this template use File | Settings | File Templates.
 */
public class PreformenceTest implements Runnable {
    final static AtomicInteger counter = new AtomicInteger();

    public static void post() throws IOException {
        HttpClient client = new HttpClient();
        client.setContentType(HttpClient.ContentType.application_x_www_form_urlencoded);
        client.setMethod("POST");
        client.setCharset("UTF-8");
        client.open("http://192.168.1.80:8080");
        //client.open("http://192.168.9.125:8080");


        client.addPostParameter("name","lamfire(小林子)");
        client.addPostParameter("age", ""+ RandomUtils.nextInt());
        client.addPostParameter("items",""+RandomUtils.nextInt());
        //client.addPostParameter("items",""+RandomUtils.nextInt());
        client.post();

        byte[] ret = client.read();
        //System.out.println("POST_RESULT["+ ret.length +"]:" + new String(ret));

    }

    @Override
    public void run() {
        try{
            post();
            counter.incrementAndGet();
        }catch (Throwable t){
            t.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Threads.scheduleWithFixedDelay(new Runnable() {
            int pre = 0;
            @Override
            public void run() {
                int cur = counter.get();
                System.out.println((cur - pre) +" /s , counter=" + cur);
                pre = cur;
            }
        },1,1,TimeUnit.SECONDS);

        int nThreads = 20;
        ThreadPoolExecutor executor = new ThreadPoolExecutor(nThreads, nThreads,0L, TimeUnit.MILLISECONDS,new LinkedBlockingQueue<Runnable>()) ;

        while(true){
            if(executor.getQueue().size() < 10000){
                executor.submit( new PreformenceTest());
            }else{
                Threads.sleep(10);
            }
        }
    }
}
