package com.example.myapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class WebConfiguration extends WebMvcConfigurationSupport {

    @Override
    protected void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
//                .allowedOrigins("http://localhost:8083/")
                .allowedOriginPatterns("*") //允许跨域的域名，可以用*表示允许任何域名使用
                .allowedMethods("*")
//                .allowedMethods("POST", "GET", "OPTIONS", "DELETE", "PUT", "PATCH")
                .allowedHeaders("*")
//                .allowedHeaders("Content-Type", "Access-Control-Allow-Headers", "Authorization", "X-Requested-With", "uid", "platform", "userToken", "deviceInfo")
                .allowCredentials(true)
                .maxAge(3600);


        // add external api Mapping
//        registry.addMapping("/api/*")
//                .allowedOriginPatterns("http://10.1.1.1:8888")
//                .allowedMethods("POST", "GET", "OPTIONS", "DELETE", "PUT", "PATCH")
//                .allowedHeaders("Content-Type", "Access-Control-Allow-Headers", "Authorization", "X-Requested-With")
//                .allowCredentials(true)
//                .maxAge(3600);
        super.addCorsMappings(registry);
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        // swagger静态资源
        registry.addResourceHandler("/swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");

        super.addResourceHandlers(registry);
    }
}
