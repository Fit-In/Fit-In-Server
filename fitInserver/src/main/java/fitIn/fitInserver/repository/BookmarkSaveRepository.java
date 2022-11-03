package fitIn.fitInserver.repository;

import fitIn.fitInserver.domain.Account;
import fitIn.fitInserver.domain.News;
import fitIn.fitInserver.domain.bookmark.BookmarkSave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookmarkSaveRepository extends JpaRepository<BookmarkSave, Long> {


    boolean existsBySave_IdAndBookmark_id(Long save_id,Long bookmark_id);

}
