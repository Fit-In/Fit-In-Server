package fitIn.fitInserver.service;


import fitIn.fitInserver.domain.Account;
import fitIn.fitInserver.dto.AccountResponseDto;
import fitIn.fitInserver.repository.AccountRepository;
import fitIn.fitInserver.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountService {


    private final AccountRepository accountRepository;

//    @Transactional
//    public Long join(Account account) {
//        validateDuplicateAccount(account);
//        accountRepository.save(account);
//        return account.getId();
//    }
//
//    private void validateDuplicateAccount(Account account) {
//        Optional<Account> findEmails = accountRepository.findByEmail(account.getEmail());
//        if(!findEmails.isEmpty()){
//            throw new IllegalStateException("이미 존재하는 회원입니다.");
//        }
//    }
//
//    public List<Account> findAccounts(){
//        return accountRepository.findAll();
//    }
//
//    public Account findOne(Long accountId){
//        return accountRepository.findOne(accountId);
//    }
//
//
//    @Transactional
//    public void update(Long id, String password){
//        Account account = accountRepository.findOne(id);
//        account.setPassword(password);
//    }

    @Transactional(readOnly = true)
    public AccountResponseDto getAccountInfo(String email){
        return accountRepository.findByEmail(email)
                .map(AccountResponseDto::of)
                .orElseThrow(()->new RuntimeException("유저 정보가 없습니다."));
    }

    @Transactional(readOnly = true)
    public AccountResponseDto getMyInfo(){
        return accountRepository.findById(SecurityUtil.getCurrentMemberId())//API요청이 들어오면 Access Token을 복호화 해서 유저 정보를 꺼내 Security Context에 저장
                .map(AccountResponseDto::of)
                .orElseThrow(()->new RuntimeException("로그인 유저 정보가 없습니다."));
    }
}
