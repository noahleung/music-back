package com.graduate.musicback.config;

import com.graduate.musicback.interceptor.AdminInterceptor;
import com.graduate.musicback.interceptor.UserInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.unit.DataSize;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import javax.servlet.MultipartConfigElement;
import java.util.Collections;


@EnableWebMvc
@SpringBootConfiguration
public class WebMvcConfig implements WebMvcConfigurer {
    @Value("${file.music}")
    private String fileMusic;

    @Value("${file.picture}")
    private String filePicture;

    @Autowired
    private UserInterceptor userInterceptor;

    @Autowired
    private AdminInterceptor adminInterceptor;

    @Bean
    public RequestMappingHandlerAdapter requestMappingHandlerAdapter(@Autowired MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter, @Autowired ContentNegotiationManager mvcContentNegotiationManager) {
        RequestMappingHandlerAdapter requestMappingHandlerAdapter = new RequestMappingHandlerAdapter();
        requestMappingHandlerAdapter.setMessageConverters(Collections.singletonList(mappingJackson2HttpMessageConverter));
        requestMappingHandlerAdapter.setContentNegotiationManager(mvcContentNegotiationManager);
        return requestMappingHandlerAdapter;
    }

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        return new MappingJackson2HttpMessageConverter();
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");

        /* 区分系统环境、自定义上传映射路径 */
        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith("win")) { // 如果是Windows系统
            registry.addResourceHandler("/file/picture/**")
                    .addResourceLocations("file:"+filePicture);
            registry.addResourceHandler("/file/music/**")
                    .addResourceLocations("file:"+fileMusic);
        } else { // linux 和mac
            registry.addResourceHandler("/file/picture/**")
                    .addResourceLocations("file:/file/picture/");
            registry.addResourceHandler("/file/music/**")
                    .addResourceLocations("file:/file/music/");
        }
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 设置允许跨域的路径
        registry.addMapping("/**")
                // 设置允许跨域请求的域名
                .allowedOrigins("*")
                // 设置允许证书，不再默认开启
                .allowCredentials(true)
                // 设置允许的方法
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                // 设置跨域允许时间
                .maxAge(60 * 60);
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //文件最大
        DataSize dataSize1= DataSize.ofBytes(1024*1024*10);
        factory.setMaxFileSize(dataSize1); //KB,MB
        /// 设置总上传数据总大小
        DataSize dataSize2= DataSize.ofBytes(1024*1024*10*2);
        factory.setMaxRequestSize(dataSize2);
        return factory.createMultipartConfig();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userInterceptor).addPathPatterns("/user/**");
        registry.addInterceptor(adminInterceptor).addPathPatterns("/admin/**");

    }
}
