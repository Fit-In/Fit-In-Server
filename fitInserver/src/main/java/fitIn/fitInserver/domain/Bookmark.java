//package fitIn.fitInserver.domain;
//
//
//import lombok.AccessLevel;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import org.hibernate.annotations.BatchSize;
//
//import javax.persistence.*;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static javax.persistence.FetchType.*;
//
//@Entity
//@Table(name = "bookmarks")
//@Getter @Setter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//public class Bookmark {
//
//    @Id
//    @GeneratedValue
//    @Column(name = "bookmark_id")
//    private Long id;
//
//    @ManyToOne(fetch = LAZY)
//    @JoinColumn(name = "account_id")
//    private Account account;
//
//    @BatchSize(size = 1000)
//    @OneToMany(mappedBy = "bookmark", cascade = CascadeType.ALL)
//    private List<BookmarkNews> bookmarkNews = new ArrayList<>();
//
//
//}
