package fitIn.fitInserver.jwt;

import fitIn.fitInserver.dto.TokenDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;



@Component
public class TokenProvider {

    private final Logger logger = LoggerFactory.getLogger(TokenProvider.class);

    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30; //30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7; //7일


    private final Key key;

    public TokenProvider(
            @Value("${jwt.secret}") String secretKey) {//yml에서 secret값 가져옴
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);//Secret 값을 Base64 DECODE해 key변수에 할당
        this.key = Keys.hmacShaKeyFor(keyBytes);//base64를 byte[]로 변환, byte[]로 key 생성
    }

    //https://tansfil.tistory.com/59
    //Authentication 유저 정보를 받아서 AccessToken, RefreshToken 생성
    public TokenDto generateTokenDto(Authentication authentication){
        // 권한들 가져오기
        String authorities = authentication.getAuthorities().stream()// authentication객체(현재 접근하는 주체의 정보의 권한을 담음) 받아 jwt토큰 생성, return
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));//String 사이마다 , 추가

        long now = (new Date()).getTime();

        // Access Token 생성 - 유저와 권한정보를 담음
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);//토큰 만료시간 설정 30일
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())     //token 제목-payload, username으로 MemberID 저장
                .claim(AUTHORITIES_KEY, authorities)       //payload(body)부분(claim)-정보 하나하나를 claim이라 부름
                .setExpiration(accessTokenExpiresIn)        //유효시간을 정해주고
                .signWith(key, SignatureAlgorithm.HS512)   //토큰 생성, 토큰은 String 형태로 생성된다 -header
                .compact();//토큰 생성, 토큰은 String 형태로 생성된다

        // Refresh Token 생성 - 아무런 정보 없이 만료일자만 담음
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))//토큰 만료시간 설정 7일
                .signWith(key, SignatureAlgorithm.HS512)//토큰 생성, 토큰은 String 형태로 생성된다 -header
                .compact();//토큰 생성

        return TokenDto.builder() //생성한 토큰을 TokenDto로 반환해줌
                .grantType(BEARER_TYPE)//JWT 사용 위해 인증타입 bearer 사용
                .accessToken(accessToken)//accessToken
                .accessTokenExpiresIn(accessTokenExpiresIn.getTime())//accessToken 만료시간
                .refreshToken(refreshToken)//refreshToken
                .build();
    }
//인증에 성공할 경우 이 메서드를 통해 토큰에서 인증권한을 추출하고, Authentication 객체를 만들어서 그것을 SecurityContext에(인메모리 세션저장소) 저장합니다.
    public Authentication getAuthentication(String accessToken) {

        Claims claims = parseClaims(accessToken);// 토큰 복호화

        if (claims.get(AUTHORITIES_KEY) == null) {//권한 없을때 예외 처리
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());//람다와 stream을 이용해서 위에 jwt에서 뽑아낸 authroities 복구 시킴

        // UserDetails 객체를 만들어서 Authentication 리턴
        UserDetails principal = new User(claims.getSubject(), "", authorities);//UserDetails객체에 Jwt에서 다시 복호화한 정보 저장,
//SecurityContext를 사용하기 위한 절차로 UserDetails객체를 생성해서 UsernamePasswordAuthenticationToken으로 반환
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);//principal(접근 주체), credentials, authroites
    }

    public boolean validateToken(String token) {// 토큰을 받아 유효성 검사,만료 일자 확인, 문제 있으면 false, 정상이면 true
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);//토큰을 jwt로 파싱해서 정상인지 확인, 아니면 Exception 발생으로 검증
            return true;//정상
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            logger.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            logger.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            logger.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            logger.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;//문제 있음
    }

    private Claims parseClaims(String accessToken) {// 만료된 토큰이어도 정보를 꺼내기 위해서 따로 메서드 분리
        try {
            return Jwts//Claims : jwt의 속성 정보 , Json map 형식의 인터페이스
                    .parserBuilder()//JwtParseBuilder 객체 생성
                    .setSigningKey(key)//SecretKey or asymmetric public key 설정
                    .build()//thread-safe한 jwtParser  생성- jwt 복호화라 보면 됨
                    .parseClaimsJws(accessToken)//원래의 jwt 생성
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

}


