package fitIn.fitInserver.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fitIn.fitInserver.repository.SaveRepository;
import fitIn.fitInserver.service.BookmarkService;
import fitIn.fitInserver.service.SaveService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookmarkSave {

    @Id @GeneratedValue
    @Column(name = "bookmark_save_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "save_id")
    private Save save;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookmark_id")
    private Bookmark bookmark;



    public static BookmarkSave createBookmarkSave(Save save){
        BookmarkSave bookmarkSave = new BookmarkSave();

        bookmarkSave.setSave(save);
        return bookmarkSave;
    }





}
