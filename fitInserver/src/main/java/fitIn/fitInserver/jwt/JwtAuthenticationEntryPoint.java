package fitIn.fitInserver.jwt;


import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {//유저 정보 없이 접근하면 SC_UNAUTHORIZED (401) 응답을 내려준다
    //AuthenticationEntryPoint - 스츠링 시큐리티 컨텍스트 내에 존재하는 인증절차 중, 틴증과정이 실패하거나 인증헤더(Authroization)을 보내지 않을 때 401응답갔을 던지는데 이를 해결
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {//401이 떨어질만한 에러가 발생할 경우 commerce 메소드가 실행
        // 유효한 자격증명을 제공하지 않고 접근하려 할때 401
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);//자격없음을 반환해줌
    }
}