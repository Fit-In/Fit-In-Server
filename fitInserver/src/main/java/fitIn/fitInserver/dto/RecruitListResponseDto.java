package fitIn.fitInserver.dto;

import fitIn.fitInserver.domain.Recruitment;
import lombok.Getter;

@Getter
public class RecruitListResponseDto {

    private Long id;

    private String company_name;

    private String position;

    private String recruitment_type;

    private String career;

    public RecruitListResponseDto(Recruitment entity) {
        this.id = entity.getId();
        this.company_name = entity.getCompany_name();
        this.position = entity.getPosition();
        this.recruitment_type= entity.getRecruitment_type();
        this.career = entity.getCareer();
    }


}
