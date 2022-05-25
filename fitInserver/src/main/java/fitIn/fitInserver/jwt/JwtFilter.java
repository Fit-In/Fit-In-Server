package fitIn.fitInserver.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {//filter는 요청(Request)와 응답(Response)에 대한 정보들을 변경할 수 있게 개발자들에게 제공하는 서블린 컨테이너


    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer";

    private final RedisTemplate redisTemplate;
    private final TokenProvider tokenProvider;//Filter를 통해 JWT토큰이 유효한지 검증하는 메서드

    // 실제 필터링 로직은 doFilterInternal 에 들어감
    // JWT 토큰의 인증 정보를 현재 쓰레드의 SecurityContext 에 저장하는 역할 수행

    //가입, 로그인, 재발급 제외 모든 Request 요청은 이 필터를 거쳐서 토큰 정보가 없거나 유효하지 않으면 정상적으로 수행 안됨
    //대신 직접 DB 를 조회한 것이 아니라 Access Token 에 있는 Member ID 를 꺼낸 거라서, 탈퇴로 인해 Member ID 가 DB 에 없는 경우 등 예외 상황은 Service 단에서 고려해야 한다
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {//jwt토큰의 인증정보를 현재 실행중인 securitycontext에 저장

        // 1. Request Header 에서 JWT 토큰 추출
        String token = resolveToken((HttpServletRequest) request);

        // 2. validateToken 으로 토큰 유효성 검사
        if (token != null && tokenProvider.validateToken(token)) {
            // (추가) Redis 에 해당 accessToken logout 여부 확인
            String isLogout = (String)redisTemplate.opsForValue().get(token);
            if (ObjectUtils.isEmpty(isLogout)) {
                // 토큰이 유효할 경우 토큰에서 Authentication 객체를 가지고 와서 SecurityContext 에 저장
                Authentication authentication = tokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        chain.doFilter(request, response);
    }

    //Http Request 헤더에서 토큰만 추출하기 위한 메서드
    private String resolveToken(HttpServletRequest request) {//request header에서 토큰 정보를 꺼내옴
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);//request에서 header 추출
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {//request에서 Bearer부분 이하로 붙은 token
            return bearerToken.substring(7);//파싱해서 리텅
        }
        return null;
    }
}
