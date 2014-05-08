package com.lamfire.warden.sample;


import com.lamfire.json.JSON;
import com.lamfire.warden.Action;
import com.lamfire.warden.ActionContext;
import com.lamfire.warden.DataBoundActionSupport;
import com.lamfire.warden.anno.ACTION;

import java.io.IOException;
import java.io.OutputStream;

@ACTION(path="/")
public class TestAction extends DataBoundActionSupport {

    private String name;

    private int age;

	@Override
	public void execute(ActionContext context,OutputStream writer) {
		JSON js = new JSON();
		js.put("status", 200);
		js.put("timestrap", System.currentTimeMillis());
        js.put("name",name);
        js.put("age",age);

        try {
            context.getHttpResponseWriter().write(js.toBytes());
        } catch (IOException e) {

        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
