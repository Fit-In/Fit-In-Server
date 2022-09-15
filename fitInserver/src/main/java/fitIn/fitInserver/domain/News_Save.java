package fitIn.fitInserver.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;


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
    private String category;

    @Column
    private String image_url;

    @Column
    private String link;
}
