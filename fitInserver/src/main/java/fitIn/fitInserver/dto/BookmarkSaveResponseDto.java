package fitIn.fitInserver.dto;

import lombok.Data;

@Data
public class BookmarkSaveResponseDto {

    private Long bookmarkId;
    private Long saveId;
    private String position;

    public BookmarkSaveResponseDto(Long bookmarkId, Long saveId, String position) {
        this.bookmarkId = bookmarkId;
        this.position = position;
        this.saveId = saveId;
    }
}
