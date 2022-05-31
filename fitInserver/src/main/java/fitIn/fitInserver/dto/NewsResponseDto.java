package fitIn.fitInserver.dto;

import fitIn.fitInserver.domain.News;
import lombok.Getter;


@Getter
public class NewsResponseDto {


    private Long id;

    private String 언론사;

    private String 뉴스제목;

    private String 본문내용;

    private String 카테고리;

    private String 이미지url;

    private String Link;


    public NewsResponseDto(News entity){

        this.id = entity.getId();
        this.언론사 = entity.get언론사();
        this.뉴스제목 = entity.get뉴스제목();
        this.본문내용 = entity.get본문내용();
        this.카테고리 = entity.get카테고리();
        this.이미지url = entity.get이미지url();
        this.Link = entity.getLink();
    }

}
