package fitIn.fitInserver.service;

import fitIn.fitInserver.domain.Account;
import fitIn.fitInserver.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.metadata.HanaCallMetaDataProvider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Member;
import java.util.Collections;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    @Transactional//userDetails와 Authentication의 패스워드를 비교하고 검증, loadUserByUsername타고 들어가면 DaoAuthenticationProvider
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {//메소드 오버라이드하여 로그인시에 db에서 유저정보와 권한정보를 가져오게 됨, 해당 정보를 기반으로 userDetails.User객체를 생성, 리턴
        return accountRepository.findByEmail(username)//데이터베이스에서 username을 기반으로 값을 가져오기 때문에 아이디 존재 여부도 자동으로 검증 됨
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException(username + " -> 데이터베이스에서 찾을 수 없습니다."));
    }

    private UserDetails createUserDetails(Account account){
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(account.getRole().toString());

        return new User(
                String.valueOf(account.getEmail()),
                account.getPassword(),
                Collections.singleton(grantedAuthority)
        );


    }

}
