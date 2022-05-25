package fitIn.fitInserver.config.oauth2;


import com.nimbusds.oauth2.sdk.util.StringUtils;
import fitIn.fitInserver.domain.Account;
import fitIn.fitInserver.domain.Role;
import fitIn.fitInserver.domain.auth.AuthProvider;
import fitIn.fitInserver.domain.auth.oauth2.OAuth2UserInfo;
import fitIn.fitInserver.domain.auth.oauth2.OAuth2UserInfoFactory;
import fitIn.fitInserver.domain.auth.oauth2.UserPrincipal;
import fitIn.fitInserver.exception.OAuth2AuthenticationProcessingException;
import fitIn.fitInserver.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final AccountRepository accountRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        try {
            return processOAuth2User(oAuth2UserRequest,oAuth2User);
        }catch (AuthenticationException ex){
            throw ex;
        }catch (Exception ex){
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(),oAuth2User.getAttributes());
        if(StringUtils.isBlank(oAuth2UserInfo.getEmail())){
            throw new OAuth2AuthenticationProcessingException("이메일을 찾을 수 없습니다.");
        }
        Optional<Account> accountOptional = accountRepository.findByEmail(oAuth2UserInfo.getEmail());
        Account account;
        if(accountOptional.isPresent()){
            account = accountOptional.get();
            System.out.println(oAuth2UserRequest.getClientRegistration().getRegistrationId());
            if(!account.getProvider().equals(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))){
                throw new OAuth2AuthenticationProcessingException("Looks like you're signed up with "+ account.getProvider() +
                        "account. Please use your "+ account.getProvider()+"account to login.");
            }
            account = updateExistingUser(account,oAuth2UserInfo);
        }else{
            account = registerNewAccount(oAuth2UserRequest, oAuth2UserInfo);
        }
        return UserPrincipal.create(account, oAuth2User.getAttributes());
    }

    private Account registerNewAccount(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo){
        Account account = new Account(
                oAuth2UserInfo.getName(),
                oAuth2UserInfo.getEmail(),
                oAuth2UserInfo.getId(),
                false,
                AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()),
                Role.ROLE_USER
        );
        return accountRepository.save(account);
    }

    private Account updateExistingUser(Account existingAccount, OAuth2UserInfo oAuth2UserInfo) {
        existingAccount.changeAccountOAuth(
                existingAccount.getId(),
                oAuth2UserInfo.getName(),
                existingAccount.getEmail(),
                existingAccount.getAuthId(),
                existingAccount.getProvider());
        return existingAccount;
    }
}

