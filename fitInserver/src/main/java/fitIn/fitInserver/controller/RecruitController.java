package fitIn.fitInserver.controller;


import fitIn.fitInserver.dto.*;
import fitIn.fitInserver.service.CallRecruitService;
import fitIn.fitInserver.service.RecruitService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RecruitController {

    private final CallRecruitService callRecruitService;
    private final RecruitService recruitService;

    String baemin = "https://ja5p2ijge9.execute-api.ap-northeast-2.amazonaws.com/GET/get_baemin";
    String kakao = "https://ja5p2ijge9.execute-api.ap-northeast-2.amazonaws.com/GET/get_kakao";
    String carrot = "https://ja5p2ijge9.execute-api.ap-northeast-2.amazonaws.com/GET/get_carrot";
    String naver = "https://ja5p2ijge9.execute-api.ap-northeast-2.amazonaws.com/GET/get_naver";


    @GetMapping("recruit/save")
    public String callAPI(){
        String Data="";
        Data = Data.concat(callRecruitService.call(baemin));
        Data = Data.concat(callRecruitService.call(kakao));
        Data = Data.concat(callRecruitService.call(carrot));
        Data = Data.concat(callRecruitService.call(naver));
        return Data;
    }


    @GetMapping("/recruit/{id}")
    public RecruitResponseDto findById(@PathVariable Long id){
        return recruitService.findById(id);
    }
    @GetMapping("/recruit")
    public List<RecruitListResponseDto> searchAllAsc(){
        return recruitService.findAllAsc();
    }



}
