package fitIn.fitInserver.controller;

import fitIn.fitInserver.dto.AccountRequestDto;
import fitIn.fitInserver.dto.SmsRequestDto;
import fitIn.fitInserver.service.SmsService;
import lombok.RequiredArgsConstructor;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/sms")
public class SmsController {

    private final SmsService smsService;
    @PostMapping("/memberPhoneCheck")
    public @ResponseBody String memberPhoneCheck(@RequestBody SmsRequestDto smsRequestDto) throws CoolsmsException {

        return smsService.PhoneNumberCheck(smsRequestDto.getTo());
    }
}
