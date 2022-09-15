package fitIn.fitInserver.service;

import fitIn.fitInserver.domain.*;
import fitIn.fitInserver.dto.BookmarkRequestDto;
import fitIn.fitInserver.repository.AccountRepository;
import fitIn.fitInserver.repository.BookmarkQueryRepository;
import fitIn.fitInserver.repository.BookmarkRepository;
import fitIn.fitInserver.repository.SaveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BookmarkService {

    private final AccountRepository accountRepository;
    private final SaveRepository saveRepository;
    private final BookmarkRepository bookmarkRepository;
    private final BookmarkQueryRepository bookmarkQueryRepository;

    public ResponseEntity<?> bookmarkCreate(BookmarkRequestDto bookmarkRequestDto){
        Account account = accountRepository.findByEmail(bookmarkRequestDto.getAccountEmail()).orElseThrow(EntityNotFoundException::new);
        Bookmark bookmark = Bookmark.createBookmark(account, bookmarkRequestDto.getBookmarkName());
        bookmarkRepository.save(bookmark);
        return ResponseEntity.ok("북마크 생성 완료");
    }


    public ResponseEntity<?> bookmarkAdd(BookmarkRequestDto bookmarkRequestDto) {

        Save save= saveRepository.findById(bookmarkRequestDto.getSaveId()).orElseThrow(EntityNotFoundException::new);
        Bookmark bookmark= bookmarkRepository.findById(bookmarkRequestDto.getBookmarkId()).orElseThrow(EntityNotFoundException::new);

        BookmarkSave bookmarkSave = BookmarkSave.createBookmarkSave(save);
        Bookmark bookmarkAdd = Bookmark.addBookmark(bookmark ,bookmarkSave);
        bookmarkRepository.save(bookmarkAdd);

        return ResponseEntity.ok("북마크 추가 완료");
    }

    public List<Bookmark> findBookmarks(BookmarkRequestDto bookmarkRequestDto) {
        return bookmarkQueryRepository.findAll(bookmarkRequestDto.getAccountEmail());
    }

    public Bookmark findBookmark(Long bookmarkId) {
        return bookmarkRepository.findByid(bookmarkId);
    }
}
