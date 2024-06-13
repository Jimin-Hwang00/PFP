package ddwu.wcs.pfp.domain;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Medicine implements Serializable {
    private String medicineCode;    // 약품 코드
    private String medicineName;    // 약품 이름
    private String dosage;  // 약품 용량
    private String howAdm;  // 약품 투여 방법
    private String numAdm; // 약품 투여 횟수
    private String daysAdm;    // 약품 투여 일수
}
