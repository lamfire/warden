package com.lamfire.warden.sample;

import com.lamfire.warden.Action;
import com.lamfire.warden.ActionContext;
import com.lamfire.warden.anno.ACTION;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-5-12
 * Time: 上午9:54
 * To change this template use File | Settings | File Templates.
 */
@ACTION(path="/redirect",singleton = true)
public class RedirectAction implements Action {
    @Override
    public void execute(ActionContext context) {
        context.sendRedirect("http://www.baidu.com");
    }
}
