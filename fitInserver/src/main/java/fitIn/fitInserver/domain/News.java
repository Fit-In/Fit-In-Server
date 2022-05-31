package fitIn.fitInserver.domain;


import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "news")
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//기본 키 생성을 데이터베이스에 위임
    @Column(name = "account_id")
    private Long id;

    @Column
    private String 언론사;

    @Column(length = 1000)
    private String 뉴스제목;

    @Column(length=5000)
    private String 본문내용;

    @Column
    private String 카테고리;

    @Column
    private String 이미지url;

    @Column
    private String link;
}
