package fitIn.fitInserver.controller;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import fitIn.fitInserver.dto.NewsListResponseDto;
import fitIn.fitInserver.dto.NewsRequestDto;

import fitIn.fitInserver.dto.NewsResponseDto;
import fitIn.fitInserver.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;
    @GetMapping("/news/save")
    public String callAPI(){
        HashMap<String, Object> result = new HashMap<String, Object>();
        String jsonInString="";

        try {
            HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
            factory.setConnectTimeout(5000);
            factory.setReadTimeout(5000);
            RestTemplate restTemplate = new RestTemplate(factory);

            HttpHeaders header = new HttpHeaders();
            HttpEntity<?> entity = new HttpEntity<>(header);

            String url = "https://ja5p2ijge9.execute-api.ap-northeast-2.amazonaws.com/include_news/news";
            UriComponents uri = UriComponentsBuilder.fromHttpUrl(url).build();

            ResponseEntity<Object> resultMap = restTemplate.exchange(uri.toString(), HttpMethod.GET, entity, Object.class);

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            jsonInString = mapper.writeValueAsString(resultMap.getBody());
            List<NewsRequestDto> Dtos = Arrays.asList(mapper.readValue(jsonInString, NewsRequestDto[].class));
            Dtos.forEach(d -> {
                newsService.save(d);
//                System.out.println(d.get언론사() +  " : " + d.get뉴스제목());
            });

        }catch(HttpClientErrorException| HttpServerErrorException e){
            result.put("statusCode",e.getRawStatusCode());
            result.put("body",e.getStatusCode());
            e.printStackTrace();
        }catch (Exception e){
            result.put("statusCode","999");
            result.put("body","exception 오류");
            e.printStackTrace();
        }

        return  jsonInString;
    }

    @GetMapping("/news/{id}")
    public NewsResponseDto findById(@PathVariable Long id){
        return newsService.findById(id);
    }
    @GetMapping("/news")
    public List<NewsListResponseDto> searchAllDesc(){
        return newsService.findAllDesc();
    }



}
