package cn.cqut.lgqs.admin.config;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * swagger在线文档配置<br>
 * 项目启动后可通过地址：http://host:ip/swagger-ui.html 查看在线文档
 *
 * @author CuriT
 */

@Configuration
@EnableSwagger2
@EnableSwaggerBootstrapUI
public class AdminSwagger2Configuration {
    @Bean
    public Docket adminDocket() {

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("admin")
                .apiInfo(adminApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("cn.cqut.lgqs.admin.web"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo adminApiInfo() {
        return new ApiInfoBuilder()
                .title("lgqs-admin API")
                .description("lgqs管理后台API")
                .termsOfServiceUrl("lgqs")
                .contact("")
                .version("1.0")
                .build();
    }
}
