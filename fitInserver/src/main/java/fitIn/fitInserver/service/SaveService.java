package fitIn.fitInserver.service;


import fitIn.fitInserver.domain.bookmark.News_Save;
import fitIn.fitInserver.domain.bookmark.Recruit_Save;
import fitIn.fitInserver.domain.bookmark.Save;
import fitIn.fitInserver.dto.NewsResponseDto;
import fitIn.fitInserver.dto.RecruitResponseDto;
import fitIn.fitInserver.dto.Response;
import fitIn.fitInserver.repository.SaveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SaveService {

    private final SaveRepository saveRepository;
    private final Response response;

//    @Transactional
//    public ResponseEntity<?> saveSave(Save save){
//            Long saveId= saveRepository.save(save).getId();
//        return response.success(saveId,"북마크에 데이터를 저장했습니다.",HttpStatus.OK);
//    }

    @Transactional
    public ResponseEntity<?> saveNews(Save save) {//넘어온 Dto를

        if (saveRepository.existsByTitle(save.getTitle())) {
            return response.fail("이미 존제하는 데이터입니다.", HttpStatus.BAD_REQUEST);

        } else {
            Long saveId = saveRepository.save(save).getId();
            return response.success(saveId, "북마크에 데이터를 저장했습니다.", HttpStatus.OK);
        }
    }
    @Transactional
    public ResponseEntity<?> saveRecruit(Save save) {//넘어온 Dto를

        if (saveRepository.existsByPosition(save.getPosition())) {
            return response.fail("이미 존제하는 데이터입니다.", HttpStatus.BAD_REQUEST);

        } else {
            Long saveId = saveRepository.save(save).getId();
            return response.success(saveId, "북마크 임시목록에 데이터를 저장했습니다.", HttpStatus.OK);
        }
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
                .orElseThrow(()->new IllegalArgumentException("해당 뉴스가 없습니다. id="+id));
        return new NewsResponseDto(news_save);
    }


    public Save findById(Long id){
        Optional<Save> save = saveRepository.findById(id);
        return save.get();

    }

}




