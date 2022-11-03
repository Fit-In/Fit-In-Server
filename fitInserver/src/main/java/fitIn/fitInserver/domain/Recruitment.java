package fitIn.fitInserver.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

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

    @Column
    private String company_name;

    @Column(length = 1000)
    private String position;

    @Column
    private String tag;

    @Column
    private String career;

    @Column
    private String recruitment_type;

    @Column
    private String recruitment_period;

    @Column
    private String url_link;

    @Column(length=5000)
    private String specific_info;


}
