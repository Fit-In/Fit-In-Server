package fitIn.fitInserver.controller;


import fitIn.fitInserver.dto.AccountRequestDto;
import fitIn.fitInserver.dto.AccountResponseDto;
import fitIn.fitInserver.dto.TokenDto;
import fitIn.fitInserver.dto.TokenRequestDto;
import fitIn.fitInserver.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AccountResponseDto> signup(@RequestBody AccountRequestDto accountRequestDto){
        return ResponseEntity.ok(authService.signup(accountRequestDto));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody AccountRequestDto accountRequestDto){
        return ResponseEntity.ok(authService.login(accountRequestDto));
    }
    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(@RequestBody TokenRequestDto tokenRequestDto){
        return ResponseEntity.ok(authService.reissue(tokenRequestDto));
    }
}
