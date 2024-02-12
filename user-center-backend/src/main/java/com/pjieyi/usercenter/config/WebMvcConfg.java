package com.pjieyi.usercenter.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfg implements WebMvcConfigurer {

    //@Override
    //public void addCorsMappings(CorsRegistry registry) {
    //    //设置允许跨域的路径
    //    registry.addMapping("/**")
    //            //设置允许跨域请求的域名
    //            //当**Credentials为true时，**Origin不能为星号，需为具体的ip地址【如果接口不带cookie,ip无需设成具体ip】
    //            .allowedOrigins("http://127.0.0.1:5152")
    //            //是否允许证书 不再默认开启
    //            .allowCredentials(true)
    //            //设置允许的方法
    //            .allowedMethods("*")
    //            //跨域允许时间
    //            .maxAge(3600);
    //}
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");
    }
}