package com.liuyiling.jersey.resources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by liuyl on 15/12/9.
 * 第一个Jersey框架
 */

@Component
/**
 * 加入@Scope("request")注解后，Spring会针对每一个request请求都生成新的Jersey服务类实例。
 * 但使用request scope还要在web.xml文件中加入Spring RequsetContextListener的配置:
 */
//@Scope("request")
@Path("first")
public class FirstResource {

    @Autowired
    private User user;

    @Path("firstJersey")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String firstJersey() {
        return user.sayHello();
    }

    public void setUser(User user) {
        this.user = user;
    }
}
