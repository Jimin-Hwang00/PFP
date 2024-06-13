package ddwu.wcs.pfp.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class KeyPairFileName {  // KeyPair 파일을 관리하기 위한 도메인 클래스
    private String accountId;   // 계정 ID
    private String keyPairFileName;    // KeyPair 파일명
}
