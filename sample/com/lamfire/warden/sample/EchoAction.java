package com.lamfire.warden.sample;


import com.lamfire.utils.IOUtils;
import com.lamfire.warden.Action;
import com.lamfire.warden.ActionContext;
import com.lamfire.warden.ActionSupport;
import com.lamfire.warden.anno.ACTION;

import java.io.IOException;
import java.io.OutputStream;

@ACTION(path="/echo")
public class EchoAction extends ActionSupport {

    @Override
    public void execute(ActionContext context, byte[] message, OutputStream writer) {
        try {
            IOUtils.write(context.getHttpResponseWriter(),context.getHttpRequestData());
        } catch (IOException e) {

        }
    }

}