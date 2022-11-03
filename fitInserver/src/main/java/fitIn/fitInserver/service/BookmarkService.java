package fitIn.fitInserver.service;

import fitIn.fitInserver.domain.*;
import fitIn.fitInserver.domain.bookmark.Bookmark;
import fitIn.fitInserver.domain.bookmark.BookmarkSave;
import fitIn.fitInserver.domain.bookmark.Save;
import fitIn.fitInserver.dto.BookmarkRequestDto;
import fitIn.fitInserver.dto.BookmarkSaveResponseDto;
import fitIn.fitInserver.dto.Response;
import fitIn.fitInserver.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    private final BookmarkSaveRepository bookmarkSaveRepository;
    private final Response response;

    public ResponseEntity<?> bookmarkCreate(BookmarkRequestDto bookmarkRequestDto){
        Account account = accountRepository.findByEmail(bookmarkRequestDto.getAccountEmail()).orElseThrow(EntityNotFoundException::new);
        Bookmark bookmark = Bookmark.createBookmark(account, bookmarkRequestDto.getBookmarkName());
        bookmarkRepository.save(bookmark);
        return ResponseEntity.ok("북마크 생성 완료");
    }


    public ResponseEntity<?> bookmarkAdd(BookmarkRequestDto bookmarkRequestDto) {


//        if(bookmarkSaveRepository.existsById(bookmarkRequestDto.getSaveId())){
//            return response.fail("이미 저장한 데이터입니다.", HttpStatus.BAD_REQUEST);
//        }
//        else{
            Save save= saveRepository.findById(bookmarkRequestDto.getSaveId()).orElseThrow(EntityNotFoundException::new);
            Bookmark bookmark= bookmarkRepository.findById(bookmarkRequestDto.getBookmarkId()).orElseThrow(EntityNotFoundException::new);

            if(bookmarkSaveRepository.existsBySave_IdAndBookmark_id(save.getId(),bookmark.getId())){
                return response.fail("이미 저장된 정보입니다.", HttpStatus.BAD_REQUEST);
            }
            else{
            BookmarkSave bookmarkSave = BookmarkSave.createBookmarkSave(save);
            Bookmark bookmarkAdd = Bookmark.addBookmark(bookmark, bookmarkSave);


            bookmarkRepository.save(bookmarkAdd);
            return response.success(save.getId(), "북마크에 데이터를 저장했습니다.", HttpStatus.OK);

        }
//        }
    }


    public List<Bookmark> findBookmarks(String email) {
        return bookmarkQueryRepository.findAll(email);
    }

    public List<BookmarkSaveResponseDto> findBookmark(Long bookmarkId) {
        return bookmarkQueryRepository.findBookmarkSaves(bookmarkId);
    }
}
