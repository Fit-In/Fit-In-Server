package fitIn.fitInserver.domain;


import fitIn.fitInserver.domain.auth.AuthProvider;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//기본 키 생성을 데이터베이스에 위임
    @Column(name = "account_id")
    private Long id;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private String name;

    @Column
    private String phone;

    private String authId;
    private Boolean socialCertification;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;



    public Account(String name, String email, String authId, Boolean socialCertification,AuthProvider provider, Role role) {
        this.name = name;
        this.email = email;
        this.authId = authId;
        this.socialCertification = socialCertification;
        this.provider = provider;
        this.role = role;
    }

    public void changeAccountOAuth(Long id, String name, String email, String authId, AuthProvider provider){
        this.id = id;
        this.name = name;
        this.email = email;
        this.authId = authId;
        this.provider = provider;
    }


    public Account updatePassword(String password){
        this.password = password;
        return this;
    }





}
