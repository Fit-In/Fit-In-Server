package fitIn.fitInserver.dto;


import fitIn.fitInserver.domain.News;
import fitIn.fitInserver.domain.News_Save;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewsRequestDto {

    private String press;

    private String title;

    private String content;

    private String category;

    private String image_link;

    private String link;

    private String keyword;

    private String url_link;

    private String time;

    public News toEntity(){
        return News.builder()
                .press(press)
                .title(title)
                .content(content)
                .category(category)
                .image_link(image_link)
                .url_link(url_link)
                .keyword(keyword)
                .time(time)
                .build();
    }

    public News_Save toNews_Save(){
        return News_Save.builder()
                .press(press)
                .title(title)
                .content(content)
                .category(category)
                .image_link(image_link)
                .url_link(url_link)
                .keyword(keyword)
                .time(time)
                .build();
    }
}
