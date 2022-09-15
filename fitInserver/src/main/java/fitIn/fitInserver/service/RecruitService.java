package fitIn.fitInserver.service;

import fitIn.fitInserver.domain.Recruitment;
import fitIn.fitInserver.dto.*;
import fitIn.fitInserver.repository.RecruitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecruitService {

    private final RecruitRepository recruitRepository;
    private final Response response;

    @Transactional
    public void save(RecruitRequestDto recruitRequestDto){//넘어온 Dto를

        if(recruitRepository.existsByPosition(recruitRequestDto.getPosition())){
            System.out.println("이미 존재하는 채용정보입니다.");
        }
        else{
            recruitRepository.save(recruitRequestDto.toEntity());
        }//엔티티로 바꿔서 레포지토리에 저장해됨
    }


    public RecruitResponseDto findById(Long id){
        Recruitment entity = recruitRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("해당 채용공고가 없습니다. id="+id));
        return new RecruitResponseDto(entity);
    }


    @Transactional(readOnly = true)
    public List<RecruitListResponseDto> findAllAsc(){
        return recruitRepository.findAllAsc().stream()
                .map(RecruitListResponseDto::new)
                .collect(Collectors.toList());
    }






}
