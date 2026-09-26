package com.careerpilot.backendcodebase.configration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.lang.Runtime.version;

@Configuration
public class SwaggerConfigration {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(
                new Info().title("CAREER PILOT")
                        .description("""
                            Placement preparation website for college students.
                            ### BACKEND Development Team
                            * **Jeet Dubey** -  
                            * **Abhigyan Mishra** - 
                           """)
    .version("1.0")
    .contact(
            new Contact()
            .name("JEET DUBEY")
            .email("jeetdubey05@gmail.com")));

}
}
