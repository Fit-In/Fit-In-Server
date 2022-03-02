package fitIn.fitInserver.config.auth.dto;

import fitIn.fitInserver.domain.Account;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionAccount implements Serializable {
    private String name;
    private String email;

    public SessionAccount(Account account) {
        this.name = account.getName();
        this.email = account.getEmail();
    }
}
