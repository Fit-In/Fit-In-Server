package fitIn.fitInserver.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;




@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FindEmailRequestDto {

    @NotEmpty(message = "이름은 필수 입력값입니다.")
    @Pattern(regexp = "^[가-힣]{2,5}$", message = "이름 형식에 맞지 않습니다.")
    private String name;

    @NotEmpty(message = "전화번호는 필수 입력값입니다.")
    @Pattern(regexp = "^(010|011)[-\\s]?\\d{3,4}[-\\s]?\\d{4}$", message = "전화번호 형식에 맞지 않습니다.")
    private String phone;
}
