package fitIn.fitInserver.dto;

import fitIn.fitInserver.domain.Account;
import fitIn.fitInserver.domain.Role;
import fitIn.fitInserver.domain.auth.AuthProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccountRequestDto {

    private String name;
    private String email;
    private String password;


    public Account toAccount(PasswordEncoder passwordEncoder){
        return Account.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .name(name)
                .provider(AuthProvider.local)
                .role(Role.ROLE_USER)
                .socialCertification(true)
                .build();
    }


    public UsernamePasswordAuthenticationToken toAuthentication(){
        return new UsernamePasswordAuthenticationToken(email, password);
    }
}
