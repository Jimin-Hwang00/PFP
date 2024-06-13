package ddwu.wcs.pfp.domain;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RegisterNumber implements Serializable {
    private String firstRegNo;  // 주민등록번호 앞자리
    private char[] secondRegNo; // 주민등록번호 뒷자리
}
