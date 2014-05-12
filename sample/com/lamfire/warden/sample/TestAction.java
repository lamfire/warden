package com.lamfire.warden.sample;


import com.lamfire.json.JSON;
import com.lamfire.warden.Action;
import com.lamfire.warden.ActionContext;
import com.lamfire.warden.ActionSupport;
import com.lamfire.warden.anno.ACTION;

import java.io.OutputStream;

@ACTION(path="/",enableBoundParameters = true)
public class TestAction implements Action {

    private String name;

    private int age;

    private String[] items;

	@Override
	public void execute(ActionContext context) {

        System.out.println(context.getHttpRequest().getHeaders());

		JSON js = new JSON();
		js.put("status", 200);
		js.put("timestrap", System.currentTimeMillis());
        js.put("name",name);
        js.put("age",age);
        js.put("items",items);

        try {
            context.getHttpResponseWriter().write(js.toBytes());
        } catch (Exception e) {

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

    public String[] getItems() {
        return items;
    }

    public void setItems(String[] items) {
        this.items = items;
    }
}
