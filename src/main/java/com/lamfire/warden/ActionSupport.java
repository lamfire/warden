package com.lamfire.warden;

import com.lamfire.logger.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-5-8
 * Time: 下午4:17
 * To change this template use File | Settings | File Templates.
 */
public abstract class ActionSupport implements Action {
    private static final Logger LOGGER = Logger.getLogger(ActionSupport.class);

    public void write(OutputStream output,String message) {
        if(message == null){
            return ;
        }
        try {
            output.write(message.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void write(OutputStream output,String message,Charset charset) {
        if(message == null){
            return ;
        }
        try {
            output.write(message.getBytes(charset));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void write(OutputStream output,byte[] message) {
        if(message == null){
            return ;
        }
        try {
            output.write(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public final void execute(ActionContext context) {
        execute(context,context.getHttpRequestContentAsBytes(),context.getHttpResponseWriter());
    }

    public abstract void execute(ActionContext context,byte[] message, OutputStream writer)  ;
}
