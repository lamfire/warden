package com.lamfire.warden;

import com.lamfire.utils.Maps;
import com.lamfire.utils.ObjectFactory;
import com.lamfire.utils.ObjectUtils;
import org.jboss.netty.handler.codec.http.QueryStringDecoder;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-5-8
 * Time: 下午4:38
 * To change this template use File | Settings | File Templates.
 */
public abstract class DataBoundActionSupport extends ActionSupport {


    @Override
    public final void execute(ActionContext context, byte[] message, OutputStream writer) {
        String uri = "?" + new String(message);
        QueryStringDecoder decoder = new QueryStringDecoder(uri);
        Map<String,List<String>>  parameters = decoder.getParameters();
        Map<String ,Object> map = Maps.newHashMap();
        for(Map.Entry<String,List<String>> e : parameters.entrySet()){
            List<String> list = e.getValue();
            if(list.size() == 1){
                map.put(e.getKey(),list.get(0));
            }else{
                map.put(e.getKey(),list);
            }
        }
        ObjectUtils.setProperties(this,map);
        execute(context,writer);
    }

    public abstract void execute(ActionContext context, OutputStream writer) ;
}
