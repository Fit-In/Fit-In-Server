package fitIn.fitInserver.controller;


import fitIn.fitInserver.dto.AccountResponseDto;
import fitIn.fitInserver.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/me")
    public ResponseEntity<AccountResponseDto> getMyAccountInfo(){
        return ResponseEntity.ok(accountService.getMyInfo());
    }

    @GetMapping("/{email}")
    public ResponseEntity<AccountResponseDto> getAccountInfo(@PathVariable String email){
        return ResponseEntity.ok(accountService.getAccountInfo(email));
    }



}
