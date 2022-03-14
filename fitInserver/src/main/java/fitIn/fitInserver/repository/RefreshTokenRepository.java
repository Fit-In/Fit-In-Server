package fitIn.fitInserver.repository;

import fitIn.fitInserver.domain.Account;
import fitIn.fitInserver.domain.RefreshToken;
import fitIn.fitInserver.util.SingleResultUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.core.EntityInformation;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Optional;


@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository  {



    private final EntityManager em;

    public RefreshToken save(RefreshToken refreshToken) {
        if(refreshToken.getKey() == null) {
            em.persist(refreshToken);
            return refreshToken;
        }
        else {
            return em.merge(refreshToken);
        }
    }
    public Optional<RefreshToken> findByKey(String key){
        TypedQuery<RefreshToken> query =  em.createQuery("select r from RefreshToken r where r.key = :key", RefreshToken.class) // 특정 이름의 회원을 찾음
                .setParameter("key",key);
        return SingleResultUtil.getSingleResultOrNull(query);
    }
}
