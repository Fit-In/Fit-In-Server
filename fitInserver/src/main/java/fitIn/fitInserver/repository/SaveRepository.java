package fitIn.fitInserver.repository;

import fitIn.fitInserver.domain.bookmark.Save;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SaveRepository extends JpaRepository<Save, Long> {

    public List<Save> findAll();

    Optional<Save> findById(Long id);

    boolean existsByTitle(String Title);

    boolean existsByPosition(String Title);

    boolean existsById(Long id);

}
