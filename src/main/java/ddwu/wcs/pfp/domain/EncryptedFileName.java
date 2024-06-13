package ddwu.wcs.pfp.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EncryptedFileName {    // 전자봉투 관리를 위한 도메인 클래스
    private String senderId;   // 처방전을 내리는 기관의 ID (병원)
    private String receiverId; // 처방전을 받는 기관의 ID (약국)
    private String fileName;    // 저장된 파일 이름
}
