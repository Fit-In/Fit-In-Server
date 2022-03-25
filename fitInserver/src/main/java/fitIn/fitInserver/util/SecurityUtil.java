package fitIn.fitInserver.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class SecurityUtil {

    // SecurityContext 에 유저 정보가 저장되는 시점
    // Request 가 들어올 때 JwtFilter 의 doFilter 에서 저장
    public static String getCurrentMemberId() {//Security Context에서 authentication객체를 이용해 memberID를 리턴해주는 유틸성 메소드
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();//jwtfilter의 dofilter메소드에서

        if (authentication == null || authentication.getName() == null) {
            throw  new RuntimeException("Security Context 에 인증 정보가 없습니다.");
        }

        return authentication.getName();//유저 정보에서 MemberID만 반환
    }
}
