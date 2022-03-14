package fitIn.fitInserver.domain;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@NoArgsConstructor
@Table(name = "refresh_token")
@Entity
public class RefreshToken {

    @Id
    private String key;//MemeberID값
    private String value;//Refresh Token String

    public RefreshToken updateValue(String token){
        this.value = token;
        return this;
    }
    @Builder
    public RefreshToken(String  key, String value){
        this.key = key;
        this.value = value;
    }

}
