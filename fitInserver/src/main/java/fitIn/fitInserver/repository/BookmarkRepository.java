package fitIn.fitInserver.repository;

import fitIn.fitInserver.domain.bookmark.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark,Long> {


    public Bookmark findByid(Long id);


}
