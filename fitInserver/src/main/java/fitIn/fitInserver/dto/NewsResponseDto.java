package fitIn.fitInserver.dto;

import fitIn.fitInserver.domain.News;
import lombok.Getter;


@Getter
public class NewsResponseDto {

    private Long id;

    private String press;

    private String title;

    private String content;

    private String category;

    private String image_url;

    private String link;


    public NewsResponseDto(News entity){

        this.id = entity.getId();
        this.press = entity.getPress();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.category = entity.getCategory();
        this.image_url = entity.getImage_url();
        this.link = entity.getLink();
    }

}
