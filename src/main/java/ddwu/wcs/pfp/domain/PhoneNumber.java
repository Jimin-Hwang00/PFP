package ddwu.wcs.pfp.domain;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PhoneNumber implements Serializable {
    private String firstNo; // 휴대폰 번호 첫 번째 숫자
    private String secondNo;    // 휴대폰 번호 두 번째 숫자
    private String thirdNo; // 휴대폰 번호 세 번째 숫자
}
