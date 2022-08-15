package fitIn.fitInserver.domain;


import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "recruitment")
public class Recruitment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//기본 키 생성을 데이터베이스에 위임
    @Column(name = "recruitment_id")
    private Long id;

    @Column(length = 5000)
    private String company_name;

    @Column(length = 5000)
    private String position;

    @Column(length = 5000)
    private String tag;

    @Column(length = 5000)
    private String career;

    @Column(length = 5000)
    private String recruitment_type;

    @Column(length = 5000)
    private String recruitment_period;

    @Column(length = 5000)
    private String url_link;

    @Column(length=20000)
    private String specific_info;
}
