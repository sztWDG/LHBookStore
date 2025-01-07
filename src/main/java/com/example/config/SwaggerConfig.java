package com.example.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger开发文档相关配置
 */
@Configuration
public class SwaggerConfig {
    /*
    * OpenAPI3
@Tag(name=“接口类描述")
@Operation（summary="接口方法描述")
@Parameters
@Parameter(description=“参数描述")
@Parameter(description=“参数描述")
@Parameter(hidden=true)或@Operation(hidden=true)或@Hidden
@Schema
@Schema
* */

    /**
     * 配置文档介绍以及详细信息
     *
     * @return OpenAPI
     */
    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("示例项目 API 文档")
                        .description("欢迎来到本示例项目API测试文档，在这里可以快速进行接口调试")
                        .version("1.0")
                );
    }


}
