package fitIn.fitInserver.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("R")
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Recruit_Save extends Save {
    @Column
    private String company_name;

    @Column(length = 1000)
    private String position;

    @Column
    private String tag;

    @Column
    private String career;

    @Column
    private String recruitment_type;

    @Column
    private String recruitment_period;

    @Column
    private String url_link;

    @Column(length=5000)
    private String specific_info;

}
