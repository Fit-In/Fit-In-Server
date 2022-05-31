package fitIn.fitInserver.dto;


import fitIn.fitInserver.domain.News;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewsRequestDto {

    private String 언론사;

    private String 뉴스제목;

    private String 본문내용;

    private String 카테고리;

    private String 이미지url;

    private String link;

    public News toEntity(){
        return News.builder()
                .언론사(언론사)
                .뉴스제목(뉴스제목)
                .본문내용(본문내용)
                .카테고리(카테고리)
                .이미지url(이미지url)
                .link(link)
                .build();
    }
}
