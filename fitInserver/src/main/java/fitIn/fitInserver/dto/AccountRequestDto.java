package fitIn.fitInserver.dto;

import fitIn.fitInserver.domain.Account;
import fitIn.fitInserver.domain.Role;
import fitIn.fitInserver.domain.auth.AuthProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.Collections;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccountRequestDto {


    @NotEmpty(message = "이름은 필수 입력값입니다.")
    @Pattern(regexp = "^[가-힣]{2,5}$", message = "이름 형식에 맞지 않습니다.")
    private String name;

    @NotEmpty(message = "이메일은 필수 입력값입니다.")
    @Pattern(regexp = "^[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,3}$", message = "이메일 형식에 맞지 않습니다.")
    private String email;

    @NotEmpty(message = "비밀번호는 필수 입력값입니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String password;

    @NotEmpty(message = "전화번호는 필수 입력값입니다.")
    @Pattern(regexp = "^(010|011)[-\\s]?\\d{3,4}[-\\s]?\\d{4}$", message = "전화번호 형식에 맞지 않습니다.")
    private String phone;

    public Account toAccount(PasswordEncoder passwordEncoder){
        return Account.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .name(name)
                .provider(AuthProvider.local)
                .role(Role.ROLE_USER)
                .phone(phone)
                .socialCertification(true)
                .build();
    }

    public UsernamePasswordAuthenticationToken toAuthentication(){
        return new UsernamePasswordAuthenticationToken(email, password);
    }
}
