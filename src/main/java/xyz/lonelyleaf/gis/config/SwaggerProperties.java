package xyz.lonelyleaf.gis.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * swagger配置参数
 */
@ConfigurationProperties(prefix = "swagger")
public class SwaggerProperties {
    private Boolean enable      = true;
    private String  baseUrl     = "/";
    private String  basePackage = "xyz.lonelyleaf.gis.rest";

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }
}
