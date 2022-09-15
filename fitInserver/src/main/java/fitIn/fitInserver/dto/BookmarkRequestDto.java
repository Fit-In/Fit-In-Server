package fitIn.fitInserver.dto;

import lombok.Getter;

@Getter
public class BookmarkRequestDto {

    private String accountEmail;
    private String bookmarkName;
    private Long bookmarkId;
    private Long saveId;

}
