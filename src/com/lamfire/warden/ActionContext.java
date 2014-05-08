package com.lamfire.warden;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Set;

import com.lamfire.utils.StringUtils;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.http.*;

public class ActionContext {
	private HttpRequest request;
	private HttpResponse response;
	private ChannelHandlerContext ctx;
    private ByteArrayOutputStream responseWriter = new  ByteArrayOutputStream();
    private byte[] httpRequestData;
	
	ActionContext(ChannelHandlerContext ctx,HttpRequest request){
		this.request = request;
		this.ctx = ctx;
		this.response =  new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
	}
	
	Channel getChannel(){
		return ctx.getChannel();
	}

    public synchronized byte[] getHttpRequestData(){
        if(this.httpRequestData != null){
            return this.httpRequestData;
        }
        if (request.getMethod().equals(HttpMethod.GET)) {
            String queryString = StringUtils.substringAfter(request.getUri(), "?");
            this.httpRequestData = queryString.getBytes();
        } else{
            ChannelBuffer reqBuffer = request.getContent();
            this.httpRequestData = reqBuffer.array();
        }

        return this.httpRequestData;
    }

	public String getRemoteAddress(){
		InetSocketAddress addr = (InetSocketAddress)ctx.getChannel().getRemoteAddress();
		return addr.getHostName();
	}
	
	public int getRemotePort(){
		InetSocketAddress addr = (InetSocketAddress)ctx.getChannel().getRemoteAddress();
		return addr.getPort();
	}

    private String findAddress(String forwardFor){
        if(forwardFor == null || forwardFor.length() == 0){
            return null;
        }
        String [] addresses = forwardFor.split(",");

        for(String addr : addresses){
            if(!"unknown".equalsIgnoreCase(addr)){
                return addr;
            }
        }

        return null;
    }

    public String getRealRemoteAddr() {
        String addr;
        String forwardedFor =  request.getHeader("X-Forwarded-For");
        if((addr = findAddress(forwardedFor)) != null){
            return addr.trim();
        }

        forwardedFor = request.getHeader("Proxy-Client-IP");
        if((addr = findAddress(forwardedFor)) != null){
            return addr.trim();
        }

        forwardedFor = request.getHeader("WL-Proxy-Client-IP");
        if((addr = findAddress(forwardedFor)) != null){
            return addr.trim();
        }

        return getRemoteAddress();
    }

	public String getHttpRequestHeader(String key){
		return request.getHeader(key);
	}
	
	public Set<String> getHttpRequestHeaderNames(){
		return request.getHeaderNames();
	}
	
	public String getHttpRequestUri(){
		return request.getUri();
	}
	
	public void addHttpResponseHeader(String key,Object value){
		this.response.addHeader(key, value);
	}
	
	public void setHttpResponseStatus(HttpResponseStatus status){
		this.response.setStatus(status);
	}

	public HttpRequest getHttpRequest() {
		return request;
	}

	public HttpResponse getHttpResponse() {
		return response;
	}

    public OutputStream getHttpResponseWriter() {
        return responseWriter;
    }

    byte[] getHttpResponseData(){
        return this.responseWriter.toByteArray();
    }
}
