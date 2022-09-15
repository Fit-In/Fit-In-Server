package fitIn.fitInserver.repository;

import fitIn.fitInserver.domain.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

    @Query("SELECT n FROM News n ORDER BY n.id ASC")
    List<News> findAllAsc();

    boolean existsByTitle(String Title);


}
