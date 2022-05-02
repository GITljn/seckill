package com.ljn.seckill.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
// 默认的mvc不再生效，统一在这里配置
@EnableWebMvc
// 添加配置
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private UserArgumentResolver userArgumentResolver;

    // 添加参数解析器，会对包含满足条件的参数的接口(函数)进行拦截
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(userArgumentResolver);
    }

    // 添加资源处理器，路径匹配，寻找资源
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 匹配路径，满足该模式的路径会到下面的位置找
        registry.addResourceHandler("/**")
                // 资源的地址
                .addResourceLocations("classpath:static/");
    }
}
