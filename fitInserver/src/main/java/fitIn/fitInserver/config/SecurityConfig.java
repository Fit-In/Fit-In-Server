package fitIn.fitInserver.config;

import fitIn.fitInserver.config.oauth2.CustomOAuth2UserService;
import fitIn.fitInserver.config.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import fitIn.fitInserver.config.oauth2.OAuth2AuthenticationFailureHandler;
import fitIn.fitInserver.config.oauth2.OAuth2AuthenticationSuccessHandler;
import fitIn.fitInserver.jwt.JwtAccessDeniedHandler;
import fitIn.fitInserver.jwt.JwtAuthenticationEntryPoint;
import fitIn.fitInserver.jwt.JwtFilter;
import fitIn.fitInserver.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;


@EnableWebSecurity//어노테이션 명시만으로도 springSequrityFilterChain가 자동으로 포함되어 짐 스프링 시큐리티를 사용하겠다고 선언
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final TokenProvider tokenProvider;
    private final CorsFilter corsFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final RedisTemplate redisTemplate;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {//패스워드 encoder
        return new BCryptPasswordEncoder();//실제 DB에는 비밀번호가 암호호 되어 있어서 Client에서 넘어온 사용자 입력 정보를 이용해 인증하기 위해 알고리즘 엔코더 필요
    }

    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieOAuth2AuthorizationRequestRepository(){
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

    //스프링 시큐리티 룰을 무시하게 하는 URL 규칙,/ h2 database 테스트가 원활하도록 관련 API 들은 전부 무시
    @Override
    public void configure(WebSecurity web) {//로그인 상관 없이 허용을 해 줘야할 메소드 위치
        web.ignoring()
                .antMatchers("/h2-console/**", "/favicon.ico");
    }


    @Override//요청 URI에 대한 권한 설정, 특정 기능 결과에 대한 Handler 등록, Custom Filter등록, 예외 핸들러 등 등록
    protected void configure(HttpSecurity httpSecurity) throws Exception {//인증 매커니즘 구성, 로그인URL,권한분리, logout URL등등 설정
        httpSecurity
                // token을 사용하는 방식이기 때문에 csrf를 disable합니다.
                .cors()//.configurationSource(corsConfigurationSource())
                .and()
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                //.addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)//corsfilter 추가

                .exceptionHandling()//예외처리 handler 처리
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)//유효한 자격증명을 제공하지 않고 접근하려 할때 401 Unauthorized에러를 리턴
                .accessDeniedHandler(jwtAccessDeniedHandler)//필요한 권한이 존재하지 않은 경우에 403 Forbidden에러 리턴

                // enable h2-console
                .and()
                .headers()
                .frameOptions()
                .sameOrigin()

                // 세션을 사용하지 않기 때문에 STATELESS로 설정
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .requestMatchers(CorsUtils::isCorsRequest).permitAll()
                .antMatchers("/auth/**", "/oauth2/**").permitAll()//auth로 시작하는 API 모두 허용
                .anyRequest().authenticated().and()//나머지는 인증된 사용자만 접근할 수 있다.
                .cors().and()

                .oauth2Login()
                .authorizationEndpoint().baseUri("/oauth2/authorize")
                .authorizationRequestRepository(cookieOAuth2AuthorizationRequestRepository())
                .and()
                .redirectionEndpoint().baseUri("/oauth2/callback/*")
                .and()
                .userInfoEndpoint().userService(customOAuth2UserService)
                // JwtFilter 를 addFilterBefore 로 등록했던 JwtSecurityConfig 클래스를 적용
                .and()
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler)
                .and()
                .addFilterBefore(new JwtFilter(redisTemplate,tokenProvider),UsernamePasswordAuthenticationFilter.class);//만든 jwtfilter를 addfilterBefore로 등록했던 jwtSecurityConfig 클래스도 적용
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOrigin("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}
