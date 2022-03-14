package fitIn.fitInserver.dto;

import fitIn.fitInserver.domain.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponseDto {
    private String email;

    public static AccountResponseDto of(Account account)
    {
        return new AccountResponseDto(account.getEmail());
    }
}
