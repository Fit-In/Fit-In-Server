package fitIn.fitInserver.controller;


import fitIn.fitInserver.dto.NewsListResponseDto;
import fitIn.fitInserver.dto.NewsResponseDto;
import fitIn.fitInserver.service.CallNewsService;
import fitIn.fitInserver.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class NewsController {

    private final CallNewsService callNewsService;
    private final NewsService newsService;

    String news = "https://ja5p2ijge9.execute-api.ap-northeast-2.amazonaws.com/GET/get_news2";
    @GetMapping("news/save")
    public String callAPI(){
        String Data="";
        Data = Data.concat(callNewsService.call(news));
        return Data;
    }



    @GetMapping("/news/{id}")
    public NewsResponseDto findById(@PathVariable Long id){
        return newsService.findById(id);
    }
    @GetMapping("/news")
    public List<NewsListResponseDto> searchAllAsc(){
        return newsService.findAllAsc();
    }



}
