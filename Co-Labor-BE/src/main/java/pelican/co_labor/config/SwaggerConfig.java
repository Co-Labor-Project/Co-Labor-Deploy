package pelican.co_labor.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class SwaggerConfig {

    @Bean
    public OpenAPI api() {
        Info info = new Info()
                .title("co-labor API")
                .version("V1.0");

//-------------------- 인가 방식 지정 ---------------------
        SecurityScheme auth = new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.COOKIE)
                .name("JSESSIONID");  // 쿠키 이름 설정

        SecurityRequirement securityRequirement = new SecurityRequirement().addList("Auth");  // 여기를 수정

        return new OpenAPI()
                .components(new Components().addSecuritySchemes("JSESSIONID", auth))  // 이름 수정
                .addSecurityItem(securityRequirement)
                .info(info);
    }

}
