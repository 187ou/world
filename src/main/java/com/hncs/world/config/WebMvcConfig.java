package com.hncs.world.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import org.springframework.web.servlet.config.annotation.CorsRegistry;

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * WebMvc配置类
 */
@Configuration
@EnableSwagger2WebMvc
@EnableKnife4j
public class WebMvcConfig extends WebMvcConfigurationSupport {

    @Autowired
    private JwtInterceptor jwtInterceptor;

    /**
     * 配置拦截器
     * @param registry
     */
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**") // 拦截所有/api开头的请求
                .excludePathPatterns(
                        // 仅排除公开接口（必须精确到具体路径）
                        "/api/auth/register",
                        "/api/auth/login",
                        "/api/auth/send-verification-code",
                        "/api/auth/send-register-code",
                        "/api/auth/reset-password",
                        // 注意：移除 "/api/auth/**" 这一行！！！

                        // 放行Knife4j文档相关资源（不变）
                        "/doc.html",
                        "/webjars/**",
                        "/swagger-resources/**",
                        "/v2/api-docs",
                        "/swagger-ui.html",
                        "/favicon.ico"
                );
    }


    /**
     * 通过knife4j生成接口文档
     * @return
     */
    @Bean
    public Docket docket() {
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("world接口文档")
                .version("1.0")
                .description("world接口文档")
                .build();
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.hncs.world.controller"))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(securitySchemes())
                // 2. 定义哪些接口需要鉴权（非白名单接口）
                .securityContexts(securityContexts());
        return docket;
    }

    /**
     * 定义JWT鉴权的请求头规则（名称、位置）
     */
    private List<ApiKey> securitySchemes() {
        List<ApiKey> apiKeys = new ArrayList<>();
        apiKeys.add(new ApiKey("Authorization", "Authorization", "header"));
        return apiKeys;
    }

    /**
     * 定义需要鉴权的接口范围（白名单外的接口都需要鉴权）
     */
    private List<SecurityContext> securityContexts() {
        List<SecurityContext> contexts = new ArrayList<>();
        contexts.add(
                SecurityContext.builder()
                        .securityReferences(defaultAuth())
                        // 白名单接口：不需要鉴权的接口路径
                        .forPaths(PathSelectors.regex(
                                "^(?!/api/auth/register|/api/auth/login|/api/auth/send-verification-code|/api/auth/send-register-code|/api/auth/reset-password).*$"
                        ))
                        .build()
        );
        return contexts;
    }

    /**
     * 定义鉴权的作用域（全局生效）
     */
    private List<SecurityReference> defaultAuth() {
        AuthorizationScope scope = new AuthorizationScope("global", "accessEverything");
        return Collections.singletonList(new SecurityReference("Authorization", new AuthorizationScope[]{scope}));
    }

    /**
     * 设置静态资源映射
     * @param registry
     */
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    /**
     * 扩展消息转换器，实现自定义的返回结果
     * @param converters
     */
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        //创建消息转换器对象
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        //设置对象转换器，底层使用Jackson将Java对象转为json
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        //将上面的消息转换器对象追加到mvc框架的转换器集合中
        converters.add(0,messageConverter);
    }



    /**
     * 跨域配置
     * @param registry cors注册器
     */
    @Override
    protected void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8848")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

}
