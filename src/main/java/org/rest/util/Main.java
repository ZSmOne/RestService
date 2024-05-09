package org.rest.util;//package org;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.rest.config.DatabaseConfig;
import org.rest.config.WebConfig;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.io.File;


public class Main {
    public static void main(String[] args) throws LifecycleException {

        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(DatabaseConfig.class);
        context.register(WebConfig.class);
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);
        tomcat.getConnector();
        tomcat.setHostname("localhost");
        String appBase = ".";
        tomcat.getHost().setAppBase(appBase);
        DispatcherServlet dispatcherServlet = new DispatcherServlet(context);//контекст spring
        File docBase = new File(System.getProperty("java.io.tmpdir"));
        Context context1 = tomcat.addContext("", docBase.getAbsolutePath());
        Tomcat.addServlet(context1, "dispatcherServlet", dispatcherServlet).setLoadOnStartup(1);
        context1.addServletMappingDecoded("/*", "dispatcherServlet");
        tomcat.start();
        tomcat.getServer().await();
    }
}
