package fitIn.fitInserver.repository;



import fitIn.fitInserver.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByEmail(String email);

    boolean existsByEmail(String email);

    Account getByEmail(String Id);

    public List<Account> findAll();

    Optional<Account> findById(Long id);

    Optional<Account> findByNameAndPhone(String name,String phone);

    Optional<Account> findByEmailAndNameAndPhone(String email, String name, String phone);

//    Optional<Account> updatePassword(String email, String name, Long phone);


}

