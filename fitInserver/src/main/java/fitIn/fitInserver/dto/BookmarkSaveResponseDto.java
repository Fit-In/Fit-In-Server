package fitIn.fitInserver.dto;

import lombok.Data;

@Data
public class BookmarkSaveResponseDto {

    private Long bookmarkId;
    private Long saveId;
    private String position;
    private String title;

    public BookmarkSaveResponseDto(Long bookmarkId, Long saveId, String position, String title) {
        this.bookmarkId = bookmarkId;
        this.position = position;
        this.saveId = saveId;
        this.title = title;
    }
}
