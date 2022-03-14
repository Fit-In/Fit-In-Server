package fitIn.fitInserver.dto;

import fitIn.fitInserver.domain.Account;
import fitIn.fitInserver.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccountRequestDto {

    private String name;
    private String email;
    private String password;

    public Account toAccount(PasswordEncoder passwordEncoder){
        return Account.builder()
                .name(name)
                .email(email)
                .password(passwordEncoder.encode(password))
                .role(Role.ROLE_USER)
                .build();

    }

    public UsernamePasswordAuthenticationToken toAuthentication(){
        return new UsernamePasswordAuthenticationToken(email, password);
    }
}
