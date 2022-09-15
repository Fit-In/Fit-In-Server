package fitIn.fitInserver.repository;

import fitIn.fitInserver.domain.Recruit_Save;
import fitIn.fitInserver.domain.Save;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SaveRepository extends JpaRepository<Save, Long> {

    public List<Save> findAll();

    Optional<Save> findById(Long id);

}
