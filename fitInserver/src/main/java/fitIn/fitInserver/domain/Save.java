package fitIn.fitInserver.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter @Setter
public abstract class Save {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "save_id")
    private Long id;

    @Column(length = 1000)
    private String title;

    @Column(length = 1000)
    private String position;



}


