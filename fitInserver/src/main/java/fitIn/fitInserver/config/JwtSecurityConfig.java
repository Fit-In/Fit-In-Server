package fitIn.fitInserver.config;


import fitIn.fitInserver.jwt.JwtFilter;
import fitIn.fitInserver.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    //provider, jwtFilter를 SecurityConfig에 적용할 JwtSecurityConfig, configure를 재정의해 jwtFilter을 통해 security로직에 필터를 등록
    private final TokenProvider tokenProvider;//authenticationpProvider

    @Override//스프링 시큐리티가 사용자를 인증하는 방법이 담긴 객체
    public void configure(HttpSecurity http) {//TokenProvider를 주입받아 JwtFilter를 통해 Security로직에 필터를 등록
        JwtFilter customFilter = new JwtFilter(tokenProvider);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);//직접 만든 JwtFilter 를 Security Filter 앞에 추가
        //SpringSecurity에 FilterComparator에 등록되어 있는 Filter들을 활성화, 일반적으로 CostomFilter는 comparator에 등록되어 있지않아 에러 발생
    }
}