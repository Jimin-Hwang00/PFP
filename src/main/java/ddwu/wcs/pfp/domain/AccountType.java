package ddwu.wcs.pfp.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AccountType implements Serializable { // 병원 계정과 약국 계정을 구분하기 위함
    private int typeId;
    private String typeName;    // 병원, 약국 (SampleDataProvider 참고)
}
