package com.lamfire.warden;

import com.lamfire.utils.ObjectFactory;
import com.sun.deploy.net.HttpRequest;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-5-9
 * Time: 上午10:08
 * To change this template use File | Settings | File Templates.
 */
public class DefaultActionFactory implements ActionFactory {
    private Class<? extends Action> actionClass;
    private ObjectFactory<Action> factory;


    public DefaultActionFactory(Class<? extends Action> actionClass){
        this.actionClass = actionClass;
        this.factory = new ObjectFactory<Action>((Class<Action>)actionClass);
    }

    @Override
    public Action make(ActionContext context) {
        try{
            return  factory.newInstance();
        }catch (Exception e){
              throw new RuntimeException(e);
        }
    }

    protected ObjectFactory<Action> getObjectFactory(){
       return this.factory;
    }
}
