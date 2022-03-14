package fitIn.fitInserver.repository;



import fitIn.fitInserver.domain.Account;
import fitIn.fitInserver.util.SingleResultUtil;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AccountRepository {

    private final EntityManager em;


    public Account save(Account account) {
        if(account.getId()==null) {
            em.persist(account);
            return account;
        }
        else {
            return em.merge(account);
        }
    }


    public Account findOne(Long id){
        return em.find(Account.class, id);
    }

    public Optional<Account> findById(Long id) {
        return Optional.ofNullable(em.find(Account.class, id));

    }

    public List<Account> findAll()
    {
        return em.createQuery("select a from Account a", Account.class).getResultList();
    }

    public Optional<Account> findByEmail(String email){
        TypedQuery<Account> query =  em.createQuery("select a from Account a where a.email = :email", Account.class) // 특정 이름의 회원을 찾음
                .setParameter("email",email);
        return SingleResultUtil.getSingleResultOrNull(query);
    }


    public boolean existByEmail(String email) {
        Long count = em.createQuery(
                        "select count(a) " +
                                "from Account a where a.email = :email", Long.class)
                .setParameter("email", email)
                .getSingleResult();

        return (count > 0);
    }
}

