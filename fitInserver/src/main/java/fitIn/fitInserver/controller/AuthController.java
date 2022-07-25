package fitIn.fitInserver.controller;


import fitIn.fitInserver.dto.AccountRequestDto;
import fitIn.fitInserver.dto.AccountResponseDto;
import fitIn.fitInserver.domain.auth.TokenDto;
import fitIn.fitInserver.domain.auth.TokenRequestDto;
import fitIn.fitInserver.dto.Response;
import fitIn.fitInserver.service.AccountService;
import fitIn.fitInserver.service.AuthService;
import fitIn.fitInserver.util.Helper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

//update account set phone = 01030992325 where phone is null
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    private final Response response;
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Validated AccountRequestDto accountRequestDto, Errors errors){
        if(errors.hasErrors()){
            return response.invalidFields(Helper.refineErrors(errors));
        }
        return authService.signup(accountRequestDto);
    }
    @GetMapping("/exists/{email}")
    public ResponseEntity<Boolean> checkEmailDuplicate(@PathVariable String email){
        return ResponseEntity.ok(authService.checkEmailDuplicate(email));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Validated AccountRequestDto accountRequestDto,Errors errors){
        if(errors.hasErrors()){
            return response.invalidFields(Helper.refineErrors(errors));
        }
        return authService.login(accountRequestDto);
    }
    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@RequestBody @Validated TokenRequestDto tokenRequestDto,Errors errors){
        if(errors.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(errors));
        }
        return authService.reissue(tokenRequestDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody @Validated TokenRequestDto tokenRequestDto,Errors errors){
        if(errors.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(errors));
        }
        return authService.logout(tokenRequestDto);
    }


    @PostMapping("/find/email")
    public ResponseEntity<?> findEmail(@RequestBody AccountRequestDto accountRequestDto){
        return ResponseEntity.ok(authService.findEmail(accountRequestDto));
    }

    @PostMapping("find/password")
    public ResponseEntity<?> findPassword(@RequestBody AccountRequestDto accountRequestDto){
        return ResponseEntity.ok(authService.findPassword(accountRequestDto));
    }

}
