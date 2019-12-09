package com.github.lonelyleaf.gis.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.DocExpansion;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * swagger配置，这里添加了基于jwt认证的代码
 */
@Configuration
@EnableSwagger2
@EnableConfigurationProperties(SwaggerProperties.class)
@ConditionalOnProperty(value = "swagger.enable", matchIfMissing = true)
public class SwaggerConfig {

    private final SwaggerProperties p;

    public SwaggerConfig(SwaggerProperties p) {
        this.p = p;
    }

    @Bean
    @ConditionalOnMissingBean(Docket.class)
    public Docket api() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2);
        docket.select()
                .build()
                .pathMapping("/")
                .genericModelSubstitutes(ResponseEntity.class)
                .enableUrlTemplating(false);
        //@formatter:off
        return docket
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false)
                .select()
                .paths(PathSelectors.any())
                //基于包名显示文档
                .apis(RequestHandlerSelectors.basePackage(p.getBasePackage()))
                .build();
        //@formatter:on
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("seed-demo api文档")
                .description("api文档描述，todo")
                .build();
    }

    @Bean
    public UiConfiguration uiConfig() {
        return UiConfigurationBuilder.builder()
                .docExpansion(DocExpansion.NONE)
                .build();
    }

}
