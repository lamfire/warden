package com.lamfire.warden;

import com.lamfire.logger.Logger;
import com.lamfire.utils.Maps;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-5-9
 * Time: 上午10:41
 * To change this template use File | Settings | File Templates.
 */
public class ParamBoundActionFactory extends DefaultActionFactory {
    private static final Logger LOGGER = Logger.getLogger(ParamBoundActionFactory.class);

    public ParamBoundActionFactory(Class<? extends Action> actionClass) {
        super(actionClass);
    }

    @Override
    public Action make(ActionContext context) {
        Action action =  super.make(context);
        Map<String ,Object> map = Maps.newHashMap();
        for(Map.Entry<String,List<String>> e : context.getHttpRequestParameters().entrySet()){
            List<String> list = e.getValue();
            if(list.size() == 1){
                map.put(e.getKey(),list.get(0));
            }else{
                map.put(e.getKey(),list);
            }
        }
        if(LOGGER.isDebugEnabled()){
            LOGGER.debug("[BINDING]:" + map);
        }
        super.getObjectFactory().setProperties(action,map);
        return action;
    }
}
