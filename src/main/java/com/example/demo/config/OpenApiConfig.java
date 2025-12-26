package com.example.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                // 1. Project Name and Description
                .info(new Info()
                        .title("Customer Loyalty Tier Upgrader API")
                        .version("1.0")
                        .description("API documentation for Customer Loyalty project"))
                // 2. Your Specific Server URL (Preserved)
                .servers(List.of(
                        new Server().url("https://9202.408procr.amypo.ai/")
                ))
                // 3. Add Authorize Padlock (Security Requirement)
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                // 4. Define JWT Configuration
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("Bearer Authentication", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}
// package com.example.demo.config;

// import io.swagger.v3.oas.models.OpenAPI;
// import io.swagger.v3.oas.models.servers.Server;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import java.util.List;

// @Configuration
// public class OpenApiConfig {

//     @Bean
//     public OpenAPI customOpenAPI() {
//         return new OpenAPI()
//                 // You need to change the port as per your server
//                 .servers(List.of(
//                         new Server().url("https://9226.408procr.amypo.ai/")
//                 ));
//         }
// }











// package com.example.demo.config;

// import io.swagger.v3.oas.models.Components;
// import io.swagger.v3.oas.models.OpenAPI;
// import io.swagger.v3.oas.models.security.SecurityRequirement;
// import io.swagger.v3.oas.models.security.SecurityScheme;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;

// @Configuration
// public class OpenApiConfig {

//     @Bean
//     public OpenAPI customOpenAPI() {
//         return new OpenAPI()
//                 .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
//                 .components(new Components().addSecuritySchemes("Bearer Authentication", 
//                     new SecurityScheme()
//                         .name("Bearer Authentication")
//                         .type(SecurityScheme.Type.HTTP)
//                         .scheme("bearer")
//                         .bearerFormat("JWT")));
//     }
// }







// package com.example.demo.config;

// import io.swagger.v3.oas.models.Components;
// import io.swagger.v3.oas.models.OpenAPI;
// import io.swagger.v3.oas.models.security.SecurityRequirement;
// import io.swagger.v3.oas.models.security.SecurityScheme;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;

// @Configuration
// public class OpenApiConfig {

//     @Bean
//     public OpenAPI customOpenAPI() {
//         return new OpenAPI()
//                 .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
//                 .components(new Components().addSecuritySchemes("Bearer Authentication", 
//                     new SecurityScheme()
//                         .name("Bearer Authentication")
//                         .type(SecurityScheme.Type.HTTP)
//                         .scheme("bearer")
//                         .bearerFormat("JWT")));
//     }
// }