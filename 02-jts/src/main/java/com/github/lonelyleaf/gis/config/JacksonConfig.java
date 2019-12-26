package com.github.lonelyleaf.gis.config;

import com.bedatadriven.jackson.datatype.jts.JtsModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.locationtech.spatial4j.io.jackson.ShapesAsGeoJSONModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.github.lonelyleaf.gis.util.JsonUtil;

/**
 * jackson配置
 */
@Configuration
public class JacksonConfig {

    /*
     * Jackson Afterburner module to speed up serialization/deserialization.
     */
//    @Bean
//    public AfterburnerModule afterburnerModule() {
//        AfterburnerModule module = new AfterburnerModule();
//        // make Afterburner generate bytecode only for public getters/setter and fields
//        // without this, Java 9+ complains of "Illegal reflective access"
//        module.setUseValueClassLoader(false);
//        return module;
//    }

    @Bean
    public ParameterNamesModule parameterNamesModule() {
        return new ParameterNamesModule();
    }

    @Bean
    public Jdk8Module jdk8Module() {
        return new Jdk8Module();
    }

    @Bean
    public JavaTimeModule javaTimeModule() {
        return new JavaTimeModule();
    }

//    @Bean
//    public ShapesAsGeoJSONModule shapesAsGeoJSONModule(){
//        return new ShapesAsGeoJSONModule();
//    }

    @Bean
    public JtsModule jtsModule(){
        return new JtsModule();
    }

    @Bean
    public JsonUtil jsonUtil(ObjectMapper objectMapper) {
        //只是把objectmapper注入
        JsonUtil.objectMapper = objectMapper;
        return new JsonUtil();
    }

}
