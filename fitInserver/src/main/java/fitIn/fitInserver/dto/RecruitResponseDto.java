package fitIn.fitInserver.dto;


import fitIn.fitInserver.domain.Recruitment;

import lombok.Getter;



@Getter
public class RecruitResponseDto {

    private Long id;

    private String company_name;

    private String position;

    private String tag;

    private String career;

    private String recruitment_type;

    private String recruitment_period;

    private String url_link;

    private String specific_info;


    public RecruitResponseDto(Recruitment entity){

        this.id = entity.getId();
        this.company_name= entity.getCompany_name();
        this.position = entity.getPosition();
        this.tag = entity.getTag();
        this.career = entity.getCareer();
        this.recruitment_type = entity.getRecruitment_type();
        this.recruitment_period = entity.getRecruitment_period();
        this.url_link = entity.getUrl_link();
        this.specific_info = entity.getSpecific_info();

    }
}
