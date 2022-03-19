package fitIn.fitInserver.service;


import fitIn.fitInserver.domain.Account;
import fitIn.fitInserver.dto.AccountRequestDto;
import fitIn.fitInserver.dto.AccountResponseDto;
import fitIn.fitInserver.dto.TokenDto;
import fitIn.fitInserver.dto.TokenRequestDto;
import fitIn.fitInserver.jwt.TokenProvider;
import fitIn.fitInserver.repository.AccountRepository;
import fitIn.fitInserver.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.concurrent.TimeUnit;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RedisTemplate redisTemplate;



    @Transactional//회원가입해서 db에 저장하는 메서드
    public AccountResponseDto signup(AccountRequestDto accountRequestDto){
        if(accountRepository.existByEmail(accountRequestDto.getEmail())){
            throw new RuntimeException("이미 가입되어 있는 유저입니다");
        }

        Account account = accountRequestDto.toAccount(passwordEncoder);//요청받아서 들어온 정보를 암호화
        return AccountResponseDto.of(accountRepository.save(account));//DB에 저장

    }

    //jwt검증후 로그인 메서드
    @Transactional
    public TokenDto login(AccountRequestDto accountRequestDto){
        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성, 아직 인증 완료된 객체가 아님, AuthenticationManger에서 authenticate메소드의 파라미터로 넘겨, 검증 후에 Authentication을 받음
        UsernamePasswordAuthenticationToken authenticationToken = accountRequestDto.toAuthentication();//Authentication는 authenticate하나만 구현 된 인터페이스, 내부 수행 검증 과정은 CustomUserDetailsService에서 구현

        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        // 인증 완료된 authentication에는 MemberId가 들어 잇음
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);//AccessToken, RefreshToken생성

        //refreshtoken db저장 방식
//        // 4. RefreshToken 저장
//        RefreshToken refreshToken = RefreshToken.builder()
//                .key(authentication.getName())
//                .value(tokenDto.getRefreshToken())
//                .build();
//
//        refreshTokenRepository.save(refreshToken);//일단 refreshToken을 DB에 저장, 나중에 reddis로 구현 해야함

        //4.RefreshToken Redis 저장(expirationTime 설정을 통해서 자동으로 삭제)
        redisTemplate.opsForValue()
                .set("RT:" + authentication.getName(), tokenDto.getRefreshToken(), tokenDto.getRefreshTokenExpiresIn(), TimeUnit.MILLISECONDS);
        // 5. 토큰 발급
        return tokenDto;//생성된 토큰 정보 클라이언트에 전달
    }


    //토큰 재발급 메서드
    @Transactional
    public TokenDto reissue(TokenRequestDto tokenRequestDto){//TokenRequestDto에서 AccessToken+ RefreshToken을 받아옴
        //1. Refresh Token 검증
        if(!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())){
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");//RefreshToken 만료여부 먼저 검사
        }
        // 2. Access Token 에서 Member ID 가져오기
        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());//AccessToken복호화해서 클라이언트가 보낸 유저정보(MemberId)가져오고

        //db저장소 refreshtoken 저장 방식
//        // 3. 저장소에서 Member ID 를 기반으로 Refresh Token 값 가져옴
//
//        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())//db에 있는 RefreshToekn 가져옴
//                .orElseThrow(()->new RuntimeException("로그아웃 된 사용자 입니다."));//db에 없으면 로그아웃 된거임
//
//        // 4. Refresh Token 일치하는지 검사
//
//        if(!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())){
//            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");//db에 저장된 refreshtoken과 client에서 가져온 refreshtoken을 일치하는지 검사함
//        }


//        // 5. 새로운 토큰 생성
//        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);//일치하면 로그인했을때 처럼 새로운 토큰 생성해서
//
//        // 6. 저장소 정보 업데이트
//        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());//client에 보낼 refresh토큰 업데이트
//        refreshTokenRepository.save(newRefreshToken);//db에 refreshtoken 재사용 못하게 새로 저장

        //// 3. Redis 에서 User email 을 기반으로 저장된 Refresh Token 값을 가져옵니다.
        String refreshToken = (String)redisTemplate.opsForValue().get("RT:" + authentication.getName());
        if(ObjectUtils.isEmpty(refreshToken)){
            throw new RuntimeException("로그아웃 된 사용자 입니다.");
        }
        if(!refreshToken.equals(tokenRequestDto.getRefreshToken())){
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }
        //4. 새로운 토큰 생성
         TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        //5. RefreshToken Redis 업데이트
        redisTemplate.opsForValue()
                .set("RT:" + authentication.getName(), tokenDto.getRefreshToken(),tokenDto.getRefreshTokenExpiresIn(),TimeUnit.MILLISECONDS);

        // 토큰 발급
        return tokenDto;//client에 새로운 토큰 전달

    }
    @Transactional
    public TokenDto logout(TokenRequestDto tokenRequestDto){
        //1. AccessToken 검증
        if(!tokenProvider.validateToken(tokenRequestDto.getAccessToken())){
            throw new RuntimeException("잘못된 요청입니다.");
        }
        //2. AccessToken에서 User email을 가져온다
        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        //3. Redis에서 해당 User email로 저장된 Refresh Token 이 있는지 확인 후 있을 경우 삭제합니다.
        if(redisTemplate.opsForValue().get("RT:" + authentication.getName())!=null){
            redisTemplate.delete("RT:" +authentication.getName());//RefreshToken 삭제
        }

        //4. 해당 Access Token 유효시간 가지고 와서 Blacklist로 저장
        Long expiration = tokenProvider.getExpiration(tokenRequestDto.getAccessToken());
        redisTemplate.opsForValue()
                .set(tokenRequestDto.getAccessToken(),"logout",expiration,TimeUnit.MILLISECONDS);

        throw new RuntimeException("로그아웃 되었습니다.");
    }
}
