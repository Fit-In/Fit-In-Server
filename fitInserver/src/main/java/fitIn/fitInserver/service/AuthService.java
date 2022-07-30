package fitIn.fitInserver.service;


import fitIn.fitInserver.domain.Account;
import fitIn.fitInserver.dto.Request.FindEmailRequestDto;
import fitIn.fitInserver.dto.Request.FindPasswordRequestDto;
import fitIn.fitInserver.dto.Request.LoginRequestDto;
import fitIn.fitInserver.dto.Request.SignupRequestDto;
import fitIn.fitInserver.domain.auth.TokenDto;
import fitIn.fitInserver.domain.auth.TokenRequestDto;
import fitIn.fitInserver.dto.Response;
import fitIn.fitInserver.jwt.TokenProvider;
import fitIn.fitInserver.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.concurrent.TimeUnit;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RedisTemplate redisTemplate;
    private final Response response;
    private final MailService mailService;

    private final Logger logger = LoggerFactory.getLogger(AuthService.class);
    @Transactional//회원가입해서 db에 저장하는 메서드
    public ResponseEntity<?> signup(SignupRequestDto accountRequestDto){
        if(accountRepository.existsByEmail(accountRequestDto.getEmail())){
            return response.fail("이미 회원가입된 이메일입니다.", HttpStatus.BAD_REQUEST);
        }
        Account account = accountRequestDto.toAccount(passwordEncoder);//요청받아서 들어온 정보를 암호화
        accountRepository.save(account);//DB에 저장
        return response.success("회원가입에 성공했습니다.");
    }

    //jwt검증후 로그인 메서드
    @Transactional
    public ResponseEntity<?> login(LoginRequestDto loginRequestDto){


        if (accountRepository.findByEmail(loginRequestDto.getEmail()).orElse(null) == null) {
            return response.fail("해당하는 유저가 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        // 첫 번째로 클라이언트에서 넘겨받은 ID/PW 를 기반으로 AuthenticationToken 생성한다
        // 이는 아직 인증 완료된 객체가 아니며 두 번째 단계인 AuthenticationManger에서 authenticate메소드를 통해 검증을 통과해야만 JWT토큰이 생성된다
        UsernamePasswordAuthenticationToken authenticationToken = loginRequestDto.toAuthentication();//Authentication는 authenticate하나만 구현 된 인터페이스, 내부 수행 검증 과정은 CustomUserDetailsService에서 구현

        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        // 인증 완료된 authentication에는 MemberId가 들어 잇음
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);//AccessToken, RefreshToken생성

        //4.RefreshToken Redis 저장(expirationTime 설정을 통해서 자동으로 삭제)
        redisTemplate.opsForValue()
                .set("RT:" + authentication.getName(), tokenDto.getRefreshToken(), tokenDto.getRefreshTokenExpiresIn(), TimeUnit.MILLISECONDS);
        // 5. 토큰 발급

        return response.success(tokenDto, "로그인에 성공했습니다.", HttpStatus.OK);
    }


    //토큰 재발급 메서드
    @Transactional
    public ResponseEntity<?> reissue(TokenRequestDto tokenRequestDto){//TokenRequestDto에서 AccessToken+ RefreshToken을 받아옴
        //1. Refresh Token 검증
        if(!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())){
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");//RefreshToken 만료여부 먼저 검사
        }
        // 2. Access Token 에서 Member ID 가져오기
        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());//AccessToken복호화해서 클라이언트가 보낸 유저정보(MemberId)가져오고

        //// 3. Redis 에서 User email 을 기반으로 저장된 Refresh Token 값을 가져옵니다.
        String refreshToken = (String)redisTemplate.opsForValue().get("RT:" + authentication.getName());
        if(ObjectUtils.isEmpty(refreshToken)){
            return response.fail("로그아웃 된 사용자 입니다.", HttpStatus.BAD_REQUEST);
        }
        if(!refreshToken.equals(tokenRequestDto.getRefreshToken())){
            return response.fail("토큰의 유저 정보가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
        //4. 새로운 토큰 생성
         TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        //5. RefreshToken Redis 업데이트
        redisTemplate.opsForValue()
                .set("RT:" + authentication.getName(), tokenDto.getRefreshToken(),tokenDto.getRefreshTokenExpiresIn(),TimeUnit.MILLISECONDS);

        // 토큰 발급
        return response.success(tokenDto, "Token 정보가 갱신되었습니다.", HttpStatus.OK);

    }


    @Transactional
    public ResponseEntity<?> logout(TokenRequestDto tokenRequestDto){
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

        return response.success("로그아웃 되었습니다.");

    }


    @Transactional
    public boolean checkEmailDuplicate(String email){
        return accountRepository.existsByEmail(email);
    }

    public ResponseEntity<?> findEmail(FindEmailRequestDto findEmailRequestDto) {
        if (accountRepository.findByNameAndPhone(findEmailRequestDto.getName(), findEmailRequestDto.getPhone()).orElse(null) == null){
            return response.fail("해당하는 유저가 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        String email = accountRepository.findByNameAndPhone(findEmailRequestDto.getName(), findEmailRequestDto.getPhone())
                .orElseThrow(() -> new UsernameNotFoundException("사용자 정보가 없습니다.")).getEmail();

        return response.success(email, "이메일 찾기를 성공하였습니다.", HttpStatus.OK);
    }


    public ResponseEntity<?> findPassword(FindPasswordRequestDto findPasswordRequestDto){
        if (accountRepository.findByEmailAndNameAndPhone(findPasswordRequestDto.getEmail(), findPasswordRequestDto.getName(), findPasswordRequestDto.getPhone()).orElse(null) == null) {
            return response.fail("해당하는 유저가 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
        Account account = accountRepository.findByEmailAndNameAndPhone(findPasswordRequestDto.getEmail(), findPasswordRequestDto.getName(), findPasswordRequestDto.getPhone())
                .orElseThrow(()->new UsernameNotFoundException("사용자 정보가 없습니다."));
        String tempPassword = randomPw();
        account.updatePassword(passwordEncoder.encode(tempPassword));

        mailService.sendEmail(findPasswordRequestDto.getEmail(),"HANDIT 임시비밀번호 발급","인증번호는 "+tempPassword+"입니다.");
        accountRepository.save(account);
        log.info("임시 비밀번호 발급. new Password={}",tempPassword);

        return response.success(tempPassword, "임시 비밀번호가 발급되었습니다.", HttpStatus.OK);

    }




    private String randomPw() {
        char[] pwCollectionSpCha = new char[]{'!', '@', '#', '$', '%', '^', '&', '*', '(', ')'};
        char[] pwCollectionNum = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0',};
        char[] pwCollectionAll = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                '!', '@', '#', '$', '%', '^', '&', '*', '(', ')'};
        return getRandPw(1, pwCollectionSpCha) + getRandPw(8, pwCollectionAll) + getRandPw(1, pwCollectionNum);
    }
    private String getRandPw(int size, char[] pwCollection) {
        StringBuilder ranPw = new StringBuilder();
        for (int i = 0; i < size; i++) {
            int selectRandomPw = (int) (Math.random() * (pwCollection.length));
            ranPw.append(pwCollection[selectRandomPw]);
        }
        return ranPw.toString();
    }
}
