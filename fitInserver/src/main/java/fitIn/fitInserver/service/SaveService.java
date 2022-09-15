package fitIn.fitInserver.service;


import fitIn.fitInserver.domain.News_Save;
import fitIn.fitInserver.domain.Recruit_Save;
import fitIn.fitInserver.domain.Save;
import fitIn.fitInserver.dto.NewsResponseDto;
import fitIn.fitInserver.dto.RecruitResponseDto;
import fitIn.fitInserver.dto.Response;
import fitIn.fitInserver.repository.SaveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SaveService {

    private final SaveRepository saveRepository;
    private final Response response;

    @Transactional
    public ResponseEntity<?> saveSave(Save save){
            Long saveId= saveRepository.save(save).getId();
        return response.success(saveId);
    }
    public List<Save> findRecruit(){
        return saveRepository.findAll();
    }

    public RecruitResponseDto findRecruit(Long id){
        Recruit_Save recruit_save = (Recruit_Save) saveRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("해당 채용공고가 없습니다. id="+id));
        return new RecruitResponseDto(recruit_save);
    }
    public NewsResponseDto findNews(Long id){
        News_Save news_save = (News_Save) saveRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("해당 뉴가가 없습니다. id="+id));
        return new NewsResponseDto(news_save);
    }
}
