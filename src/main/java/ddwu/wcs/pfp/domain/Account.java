package ddwu.wcs.pfp.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Account {
    private String id;
    private String password;
    private int typeId;     // 병원 계정, 약국 계정 구분 (UserType 참고)
    private String name;    // 병원 혹은 약국의 이름
    private String address;     // 병원 혹은 약국의 주소 (지번)
    private double latitude;    // 병원 혹은 약국의 위도 값 (추후에 지도 기능을 추가하기 위함)
    private double longitude;   // 병원 혹은 약국의 경도 값 (추후에 지도 기능을 추가하기 위함)
}
