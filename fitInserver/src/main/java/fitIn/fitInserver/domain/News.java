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
    @Column(name = "news_id")
    private Long id;

    @Column
    private String press;

    @Column(length = 1000)
    private String title;

    @Column(length=10000)
    private String content;

    @Column
    private String keyword;

    @Column
    private String category;

    @Column
    private String image_link;

    @Column
    private String url_link;

    @Column
    private String time;

}
