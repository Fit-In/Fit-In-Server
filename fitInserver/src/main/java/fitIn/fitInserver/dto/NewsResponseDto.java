package fitIn.fitInserver.dto;

import fitIn.fitInserver.domain.News;
import fitIn.fitInserver.domain.News_Save;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Getter
public class NewsResponseDto {

    private Long id;

    private String press;

    private String title;

    private String content;

    private String category;

    private String image_link;

    private String link;

    private String keyword;

    private String url_link;

    private String time;



    public NewsResponseDto(News entity){

        this.id = entity.getId();
        this.press = entity.getPress();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.category = entity.getCategory();
        this.image_link = entity.getImage_link();
        this.url_link = entity.getUrl_link();
        this.keyword = entity.getKeyword();
        this.time = entity.getTime();
    }

    public NewsResponseDto(News_Save entity){

        this.id = entity.getId();
        this.press = entity.getPress();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.category = entity.getCategory();
        this.image_link = entity.getImage_link();
        this.url_link = entity.getUrl_link();
        this.keyword = entity.getKeyword();
        this.time = entity.getTime();
    }
}
