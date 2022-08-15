package fitIn.fitInserver.repository;

import fitIn.fitInserver.domain.Recruitment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecruitRepository extends JpaRepository<Recruitment, Long> {

    @Query("SELECT n FROM Recruitment n ORDER BY n.id ASC")
    List<Recruitment> findAllAsc();
}
