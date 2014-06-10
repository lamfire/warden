package com.lamfire.warden;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
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
    private byte[] httpRequestContentAsBytes;
    private QueryStringDecoder queryStringDecoder;
    private Map<String,List<String>> httpRequestParameters;
	
	ActionContext(ChannelHandlerContext ctx,HttpRequest request){
		this.request = request;
		this.ctx = ctx;
		this.response =  new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        this.queryStringDecoder = new QueryStringDecoder(request.getUri());
	}
	
	Channel getChannel(){
		return ctx.getChannel();
	}

    public void writeResponse(String message) {
        if(message == null){
            return ;
        }
        writeResponse(message.getBytes());
    }

    public void writeResponse(String message,Charset charset) {
        if(message == null){
            return ;
        }
        writeResponse(message.getBytes(charset));

    }

    public void writeResponse(byte[] message) {
        if(message == null){
            return ;
        }
        try {
            this.responseWriter.write(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendRedirect(String redirectUrl){
        HttpResponseWriters.writeRedirectResponse(getChannel(),redirectUrl);
    }

    public synchronized byte[] getHttpRequestContentAsBytes(){
        if(this.httpRequestContentAsBytes != null){
            return this.httpRequestContentAsBytes;
        }
        if (request.getMethod().equals(HttpMethod.GET)) {
            String queryString = StringUtils.substringAfter(request.getUri(), "?");
            this.httpRequestContentAsBytes = queryString.getBytes();
        } else{
            ChannelBuffer reqBuffer = request.getContent();
            this.httpRequestContentAsBytes = reqBuffer.array();
        }

        return this.httpRequestContentAsBytes;
    }

    public synchronized String getHttpRequestContentAsString(){
          return new String(getHttpRequestContentAsBytes());
    }

	public String getRemoteAddress(){
		InetSocketAddress addr = (InetSocketAddress)ctx.getChannel().getRemoteAddress();
		return addr.getAddress().getHostAddress();
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
		return queryStringDecoder.getPath();
	}

    public Charset getHttpRequestCharset(){
        String charset = this.getHttpRequestHeader("Charset");
        if(charset == null){
            return Charset.defaultCharset();
        }
        return Charset.forName(charset);
    }

    public synchronized Map<String,List<String>> getHttpRequestParameters(){
        if(httpRequestParameters != null){
            return httpRequestParameters;
        }

        if (request.getMethod().equals(HttpMethod.GET)){
            this.httpRequestParameters = queryStringDecoder.getParameters();
        }
        byte[] data = this.getHttpRequestContentAsBytes();
        if(data != null){
            String queryString = "?" + new String(data);
            QueryStringDecoder decoder = new QueryStringDecoder(queryString);
            this.httpRequestParameters = decoder.getParameters();
        }
        return httpRequestParameters;
    }

    public List<String> getHttpRequestParameters(String name){
        return  getHttpRequestParameters().get(name);
    }

    public String getHttpRequestParameter(String name){
        List<String> list =  getHttpRequestParameters().get(name);
        if(!list.isEmpty()){
            return list.get(0);
        }
        return null;
    }

    public Set<String> getHttpRequestParameterNames(){
        return getHttpRequestParameters().keySet();
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
