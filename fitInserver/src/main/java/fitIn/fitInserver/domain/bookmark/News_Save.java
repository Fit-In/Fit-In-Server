package fitIn.fitInserver.domain.bookmark;

import lombok.*;

import javax.persistence.*;


@Entity
@DiscriminatorValue("N")
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class News_Save extends Save {

    @Column
    private String press;

    @Column(length = 1000)
    private String title;

    @Column(length=5000)
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
