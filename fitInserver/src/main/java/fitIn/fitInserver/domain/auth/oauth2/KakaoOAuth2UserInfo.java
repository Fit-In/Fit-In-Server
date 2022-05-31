package fitIn.fitInserver.domain.auth.oauth2;

import java.util.Map;

public class KakaoOAuth2UserInfo extends OAuth2UserInfo{
    Map<String,Object> kakaoAccount;
    Map<String,Object> kakaoProfile;
    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
        kakaoAccount = (Map<String, Object>)attributes.get("kakao_account");
        kakaoProfile = (Map<String, Object>)kakaoAccount.get("profile");
    }

    @Override
    public String getId() {
        return String.valueOf(attributes.get("id")) ;

    }

    @Override
    public String getName() {
        return String.valueOf(kakaoProfile.get("nickname"));
    }

    @Override
    public String getEmail() {
        return String.valueOf(kakaoAccount.get("email"));

    }
}
