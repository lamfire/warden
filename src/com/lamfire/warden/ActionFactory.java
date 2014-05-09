package com.lamfire.warden;

import com.sun.deploy.net.HttpRequest;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-5-9
 * Time: 上午10:06
 * To change this template use File | Settings | File Templates.
 */
public interface ActionFactory {

    public Action make(ActionContext context);

}
