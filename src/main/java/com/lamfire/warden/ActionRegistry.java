package com.lamfire.warden;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


import com.lamfire.logger.Logger;
import com.lamfire.utils.ClassLoaderUtils;
import com.lamfire.warden.anno.ACTION;

public class ActionRegistry {
	private static final Logger LOGGER = Logger.getLogger(ActionRegistry.class);
	private final Map<String,ActionFactory> mapping = new HashMap<String, ActionFactory>();

    public void mapping(Class<? extends Action> actionClass){
        ACTION actionAnno = actionClass.getAnnotation(ACTION.class);
        if(actionAnno == null){
            LOGGER.warn("["+actionClass.getName() + "] is assignable from Action,but not found 'ACTOIN' annotation.");
            return;
        }
        String uri = actionAnno.path();
        if(actionAnno.singleton() ){
            mappingSingletonAction(uri,actionClass);
        } else if(actionAnno.enableBoundParameters()){
            mappingParamBoundAction(uri,actionClass);
        } else{
            mappingAction(uri,actionClass);
        }
    }

	public void mappingAction(String uri,Class<? extends Action> actionClass){
		mapping.put(uri, new DefaultActionFactory(actionClass));
		LOGGER.info("[mappingAction]:" + uri +" -> " + actionClass.getName());
	}

    public void mappingSingletonAction(String uri,Class<? extends Action> actionClass) {
        mapping.put(uri, new SingletonActionFactory(actionClass));
        LOGGER.info("[mappingSingletonAction]:" + uri +" -> " + actionClass.getName());
    }

    public void mappingParamBoundAction(String uri,Class<? extends Action> actionClass) {
        mapping.put(uri, new ParamBoundActionFactory(actionClass));
        LOGGER.info("[mappingParamBoundAction]:" + uri +" -> " + actionClass.getName());
    }

    public ActionFactory getActionFactory(String uri){
        return mapping.get(uri);
    }
	
	public Action lookup(ActionContext context){
        ActionFactory factory = getActionFactory(context.getHttpRequestUri());
        if(factory == null){
            return null;
        }
        return factory.make(context);
	}
	
	public void mappingPackage(String packageName) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException{
		Set<Class<?>> set = ClassLoaderUtils.getClasses(packageName);
		for(Class<?> clzz : set){
			if(!Action.class.isAssignableFrom(clzz)){
				continue;
			}
            Class<? extends Action> actionClass =  (Class<? extends Action>)clzz;
            mapping(actionClass);
		}
	}
}
