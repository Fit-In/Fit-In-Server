package fitIn.fitInserver.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BookmarkResponseDto {

    private Long bookmarkId;
    private String bookmarkName;
    private String accountName;
    private LocalDateTime BookmarkDate; //주문시간
    private List<BookmarkSaveResponseDto> bookmarkSaves;

    public BookmarkResponseDto(Long bookmarkId,String bookmarkName, String accountName, LocalDateTime bookmarkDate) {
        this.bookmarkId = bookmarkId;
        this.bookmarkName = bookmarkName;
        this.accountName = accountName;
        this.BookmarkDate = bookmarkDate;
    }
}

