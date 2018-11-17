package com.mongodb;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class HelloWorldFreemarkerStyle {

    public static void main(String[] args) throws IOException, TemplateException {
        Configuration configuration = new Configuration();
        configuration.setClassForTemplateLoading(
                HelloWorldFreemarkerStyle.class, "/"
        );
        Template helloTemplate = configuration.getTemplate("hello.ftl");
        StringWriter writer = new StringWriter();
        Map<String, Object> helloMap = new HashMap<>();
        helloMap.put("name", "FreeMarker");

        helloTemplate.process(helloMap, writer);

        System.out.println("writer = [" + writer + "]");

    }
}
