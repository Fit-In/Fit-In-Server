package fitIn.fitInserver.service;

import fitIn.fitInserver.domain.News;
import fitIn.fitInserver.dto.NewsListResponseDto;
import fitIn.fitInserver.dto.NewsRequestDto;
import fitIn.fitInserver.dto.NewsResponseDto;
import fitIn.fitInserver.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;

    @Transactional
    public Long save(NewsRequestDto newsRequestDto){//넘어온 Dto를
        return newsRepository.save(newsRequestDto.toEntity()).getId();//엔티티로 바꿔서 레포지토리에 저장해됨
    }


    public NewsResponseDto findById(Long id){
        News entity = newsRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("해당 뉴스가 없습니다. id="+id));
        return new NewsResponseDto(entity);
    }


    @Transactional(readOnly = true)
    public List<NewsListResponseDto> findAllAsc(){
        return newsRepository.findAllAsc().stream()
                .map(NewsListResponseDto::new)
                .collect(Collectors.toList());
    }






}
