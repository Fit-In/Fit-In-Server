package fitIn.fitInserver.service;

import fitIn.fitInserver.domain.Account;
import fitIn.fitInserver.domain.Role;
import fitIn.fitInserver.repository.AccountRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class AccountServiceTest {

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    EntityManager em;

    @Test
    public  void 회원가입() throws Exception{

        //given
        Account account = new Account("fitin","fitin@naver.com",Role.USER);
        account.setPassword("1234");
        //when
        Long saveId = accountService.join(account);

        //then
        em.flush();
        assertEquals(account, accountRepository.findOne(saveId));

    }

    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception{
        //given
        Account account1 = new Account("fitin","fitin@naver.com",Role.USER);
        account1.setPassword("1234");

        Account account2 = new Account("fitin","fitin@naver.com",Role.USER);
        account2.setPassword("1234");

        //when
        accountService.join(account1);

        accountService.join(account2);

        //then
        fail("예외가 발생해야 한다.");

    }


}
