package fitIn.fitInserver.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepositorys extends JpaRepository<Account, Long> {

    // 이미 생성된 가입자인지 확인하는 메서드
    Optional<Account> findByEmail(String email);
}
