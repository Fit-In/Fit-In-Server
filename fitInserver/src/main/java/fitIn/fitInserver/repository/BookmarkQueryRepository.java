package fitIn.fitInserver.repository;

import fitIn.fitInserver.domain.Bookmark;
import fitIn.fitInserver.dto.BookmarkResponseDto;
import fitIn.fitInserver.dto.BookmarkSaveResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BookmarkQueryRepository {


    private final EntityManager em;


    public List<BookmarkResponseDto> findBookmarkResponse(){
        List<BookmarkResponseDto> result = findBookmark();

        result.forEach(b -> {
            List<BookmarkSaveResponseDto> bookmarkSaves =
                    findBookmarkSaves(b.getBookmarkId());
           b.setBookmarkSaves(bookmarkSaves);
        });
        return result;
    }


    private List<BookmarkResponseDto> findBookmark() {
        return em.createQuery(

                        "select new fitIn.fitInserver.dto.BookmarkResponseDto(b.id, b.bookmarkName, a.name, b.bookmarkDate)" +
                                " from Bookmark b" +
                                " join b.account a", BookmarkResponseDto.class)
                .getResultList();
    }


    public List<BookmarkSaveResponseDto> findBookmarkSaves(Long bookmarkId) {
        return em.createQuery(
                    "select new fitIn.fitInserver.dto.BookmarkSaveResponseDto(bs.bookmark.id, s.id,s.position,s.title)" +
                            " from BookmarkSave bs" +
                            " join bs.save s" +
                            " where bs.bookmark.id = : bookmarkId",BookmarkSaveResponseDto.class)
                    .setParameter("bookmarkId",bookmarkId)
                    .getResultList();
    }



    public List<Bookmark> findAll(String accountEmail) {

        String jpql = "select b From Bookmark b join b.account a";
        boolean isFirstCondition = true;
        if(StringUtils.hasText(accountEmail)){
            if(isFirstCondition){
                jpql += " where";
                isFirstCondition = false;
            }else{
                jpql += " and";
            }
            jpql += " a.email like :email";
        }


        TypedQuery<Bookmark> query = em.createQuery(jpql, Bookmark.class).setMaxResults(1000);

        if(StringUtils.hasText(accountEmail)){
            query = query.setParameter("email", accountEmail);
        }
        return query.getResultList();
    }
}

