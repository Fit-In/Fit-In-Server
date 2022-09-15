package fitIn.fitInserver.dto;


import fitIn.fitInserver.domain.Recruit_Save;
import fitIn.fitInserver.domain.Recruitment;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecruitRequestDto {


    private String company_name;

    private String position;

    private String tag;

    private String career;

    private String recruitment_type;

    private String recruitment_period;

    private String url_link;

    private String specific_info;

    public Recruitment toEntity(){
        return Recruitment.builder()
                .company_name(company_name)
                .position(position)
                .tag(tag)
                .career(career)
                .recruitment_type(recruitment_type)
                .recruitment_period(recruitment_period)
                .url_link(url_link)
                .specific_info(specific_info)
                .build();
    }


    public Recruit_Save toRecruit_Save() {
        return Recruit_Save.builder()
                .company_name(company_name)
                .position(position)
                .tag(tag)
                .career(career)
                .recruitment_type(recruitment_type)
                .recruitment_period(recruitment_period)
                .url_link(url_link)
                .specific_info(specific_info)
                .build();


    }
}
