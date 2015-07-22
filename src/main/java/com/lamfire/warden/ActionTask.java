package com.lamfire.warden;

import com.lamfire.logger.Logger;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;

class ActionTask implements Runnable{
    private static final Logger LOGGER = Logger.getLogger(ActionTask.class);
	private Action action;
	private ActionContext context;

	
	public ActionTask(ActionContext context,Action action){
		this.context = context;
		this.action = action;
	}

	@Override
	public void run() {
        boolean success = true;
        try {
            action.execute(this.context);
        } catch (Throwable t) {
            success = false;
            LOGGER.error(t.getMessage(), t);
        }

		try {
            if(success){
			    HttpResponseUtils.writeResponse(context, context.getHttpResponse(), context.getHttpResponseData());
            }else if(context.getChannel().isWritable()){
                HttpResponseUtils.writeResponseStatus(context, HttpResponseStatus.INTERNAL_SERVER_ERROR);
            }
		} catch (Throwable t) {
            LOGGER.error(t.getMessage(), t);
		}
	}

}
