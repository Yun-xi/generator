package com.shushuo.generator.config;

import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

/**
 * swagger2配置
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket privateApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("private-api")
                .apiInfo(privateApiInfo())
                .select()
                .paths(privatePath())
                .build();
    }


    @Bean
    public Docket openApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("open-api")
                .apiInfo(openApiInfo())
                .select()
                .paths(openPath())
                .build();
    }

    /**
     * Private Api Path
     */
    private Predicate<String> privatePath() {
        return or(
                regex("/user/.*")             // 用户
        );
    }

    /**
     * Open Api Path
     */
    private Predicate<String> openPath() {
        return or(
                regex("/sys/generator/.*")
        );
    }

    /**
     * Private Api Info
     */
    private ApiInfo privateApiInfo() {
        return new ApiInfoBuilder()
                .title("数说智能接口")
                .description("内部接口")
                .contact(new Contact("数说智能", "http://www.shushuo.com", "js_chenhu@njdream.cn"))
                .version("0.0.1")
                .build();
    }

    /**
     * Open Api Info
     */
    private ApiInfo openApiInfo() {
        return new ApiInfoBuilder()
                .title("数说智能接口")
                .description("开放接口")
                .contact(new Contact("数说智能", "http://www.shushuo.com", "js_chenhu@njdream.cn"))
                .version("0.0.1")
                .build();
    }
}
