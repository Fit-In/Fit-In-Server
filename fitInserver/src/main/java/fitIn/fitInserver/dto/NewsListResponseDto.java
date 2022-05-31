package fitIn.fitInserver.dto;

import fitIn.fitInserver.domain.News;
import lombok.Getter;

@Getter
public class NewsListResponseDto {

    private Long id;
    private String 언론사;

    private String 뉴스제목;

    private String 카테고리;

    public NewsListResponseDto(News entity) {
        this.id = entity.getId();
        this.언론사 = entity.get언론사();
        this.뉴스제목 = entity.get뉴스제목();
        this.카테고리 = entity.get카테고리();
    }


}
