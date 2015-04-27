package com.lamfire.warden;

public class SingletonActionFactory extends DefaultActionFactory {

    private Action instance ;

    public SingletonActionFactory(Class<? extends Action> actionClass) {
        super(actionClass);
    }

    @Override
    public synchronized Action make(ActionContext context) {
        if(instance == null){
            this.instance = super.make(context);
        }
        return this.instance;
    }
}
