package fitIn.fitInserver.controller;

import fitIn.fitInserver.dto.NewsRequestDto;
import fitIn.fitInserver.dto.NewsResponseDto;
import fitIn.fitInserver.dto.RecruitRequestDto;
import fitIn.fitInserver.dto.RecruitResponseDto;
import fitIn.fitInserver.service.SaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class SaveController {



    private final SaveService saveService;

    @PostMapping("/save/recruit")
    public ResponseEntity<?> bookmarkRecruit(@RequestBody RecruitRequestDto recruitRequestDto){

        return ResponseEntity.ok(saveService.saveSave(recruitRequestDto.toRecruit_Save()));
    }

    @PostMapping("/save/news")
    public ResponseEntity<?> bookmarkNews(@RequestBody NewsRequestDto newsRequestDto){

        return ResponseEntity.ok(saveService.saveSave(newsRequestDto.toNews_Save()));
    }

    @GetMapping("/save/recruit/{id}")
    public RecruitResponseDto findSaveRecruit(@PathVariable Long id){
        return saveService.findRecruit(id);
    }
    @GetMapping("/save/news/{id}")
    public NewsResponseDto findSaveNews(@PathVariable Long id){
        return saveService.findNews(id);
    }

}
