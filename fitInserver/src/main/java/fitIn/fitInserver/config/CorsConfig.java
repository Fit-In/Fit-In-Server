package fitIn.fitInserver.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {//(Cross-Origin Resource Sharing,CORS) 란 다른 출처의 자원을 공유할 수 있도록 설정하는 권한 체제를 말합니다.
    //따라서 CORS를 설정해주지 않거나 제대로 설정하지 않은 경우, 원하는대로 리소스를 공유하지 못하게 됩니다.

    @Bean
    public CorsFilter corsFilter() {

        CorsConfiguration config = new CorsConfiguration();

        config.addAllowedOrigin("http://localhost:8080");
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");//자원 공유를 허락할 Origin을 지정할 수 있습니다, *는 모두 허용
        config.addAllowedHeader("*");//허용할 헤더 지정
        config.addAllowedMethod("*");//허용할 HTTP method를 지정할 수 있습니다.

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config);
        return new CorsFilter(source);
    }

}
