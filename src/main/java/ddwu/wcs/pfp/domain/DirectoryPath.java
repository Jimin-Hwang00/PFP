package ddwu.wcs.pfp.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DirectoryPath {    // 저장된 파일을 관리하기 위한 도메인 클래스 (SampleDataProvider 참고)
    private int dirPathId;
    private String dirPathName; // 디렉토리 이름
    private String dirPath;     // 디렉토리 경로
}
