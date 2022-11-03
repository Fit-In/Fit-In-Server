package fitIn.fitInserver.controller;


import fitIn.fitInserver.domain.bookmark.Bookmark;
import fitIn.fitInserver.dto.BookmarkRequestDto;
import fitIn.fitInserver.dto.BookmarkResponseDto;
import fitIn.fitInserver.dto.BookmarkSaveResponseDto;
import fitIn.fitInserver.repository.BookmarkQueryRepository;
import fitIn.fitInserver.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookmarkController {
    private final BookmarkService bookmarkService;
    private final BookmarkQueryRepository bookmarkQueryRepository;

    @PostMapping ( "/bookmark/add")
    public ResponseEntity<?> BookmarkAdd(@RequestBody BookmarkRequestDto bookmarkRequestDto)
    {
        return bookmarkService.bookmarkAdd(bookmarkRequestDto);
    }


    @PostMapping ( "/bookmark/new")
    public ResponseEntity<?> BookmarkNew(@RequestBody BookmarkRequestDto bookmarkRequestDto)
    {
        return bookmarkService.bookmarkCreate(bookmarkRequestDto);
    }

//    @GetMapping("/bookmark/list")
//    public List<Bookmark> BookmarkList(@RequestBody BookmarkRequestDto bookmarkRequestDto){
//        return bookmarkService.findBookmarks(bookmarkRequestDto);
//    }
//

    @GetMapping("/bookmark/list/{email}")
    public List<Bookmark> BookmarkList(@PathVariable String email){
        return bookmarkService.findBookmarks(email);
    }


    @GetMapping("/bookmark/bookmarks")
    public List<BookmarkResponseDto> bookmarks()
    {
        return bookmarkQueryRepository.findBookmarkResponse();
    }

    @PostMapping("bookmark/{bookmarkId}")
    public List<BookmarkSaveResponseDto> findBookmark(@PathVariable("bookmarkId") Long bookmarkId){
        return bookmarkService.findBookmark(bookmarkId);
    }
}
