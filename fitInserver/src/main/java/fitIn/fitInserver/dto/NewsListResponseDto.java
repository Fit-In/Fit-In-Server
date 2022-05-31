package fitIn.fitInserver.dto;

import fitIn.fitInserver.domain.News;
import lombok.Getter;

@Getter
public class NewsListResponseDto {

    private Long id;
    private String press;

    private String title;

    private String category;

    public NewsListResponseDto(News entity) {
        this.id = entity.getId();
        this.press = entity.getPress();
        this.title = entity.getTitle();
        this.category = entity.getCategory();
    }


}
