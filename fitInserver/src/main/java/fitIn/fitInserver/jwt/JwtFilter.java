package fitIn.fitInserver.jwt;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {//filter는 요청(Request)와 응답(Response)에 대한 정보들을 변경할 수 있게 개발자들에게 제공하는 서블린 컨테이너

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    private final TokenProvider tokenProvider;//Filter를 통해 JWT토큰이 유효한지 검증하는 메서드

    // 실제 필터링 로직은 doFilterInternal 에 들어감
    // JWT 토큰의 인증 정보를 현재 쓰레드의 SecurityContext 에 저장하는 역할 수행

    //가입, 로그인, 재발급 제외 모든 Request 요청은 이 필터를 거쳐서 토큰 정보가 없거나 유효하지 않으면 정상적으로 수행 안됨
    //대신 직접 DB 를 조회한 것이 아니라 Access Token 에 있는 Member ID 를 꺼낸 거라서, 탈퇴로 인해 Member ID 가 DB 에 없는 경우 등 예외 상황은 Service 단에서 고려해야 한다
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {//jwt토큰의 인증정보를 현재 실행중인 securitycontext에 저장

        String jwt = resolveToken(request);  // 1. Request Header 에서 토큰을 꺼냄

        //log보기 위한 선언
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String requestURI = httpServletRequest.getRequestURI();//요청 URI 저장

        // 2. validateToken 으로 토큰 유효성 검사
        // 정상 토큰이면 해당 토큰으로 Authentication 을 가져와서 SecurityContext 에 저장
        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {//토큰의 유효성 검사, 정상이면 jwt 토큰의 인증 정보를 SecrutiyContext(현재 실행중인 스레드)에 저장
            Authentication authentication = tokenProvider.getAuthentication(jwt);//provider에서 권한 얻어와서저장
            SecurityContextHolder.getContext().setAuthentication(authentication);//얻은 권한 securitycontextholder에 저장
            logger.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);//성공 로그찍어줌, 요청이 정상적이라면 SecurityContext에 MemberID가 존재한다는 것 보장
        } else {
            logger.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);//실패 로그와 URI 찍어줌
        }

        filterChain.doFilter(request, response);
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
