package ddwu.wcs.pfp.dao;

import ddwu.wcs.pfp.domain.EncryptedFileName;
import ddwu.wcs.pfp.sampledata.SampleDataProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class EncryptedFileNameDao {
    @Autowired
    private SampleDataProvider sampleData;  // Mock Data 주입

    // EncryptedPrescription 데이터 추가
    public void insertEncryptedFile(EncryptedFileName ef) {
        sampleData.getEfList().add(ef);
    }   // EncryptedFile 추가

    // 송신자 ID를 통해 암호화된 처방전 파일 이름 가져오기
    public List<EncryptedFileName> findEncryptedFileByReceiverId(String receiverId) {   // 수신자 ID로 EncryptedFile 반환
        List<EncryptedFileName> epList = sampleData.getEfList();
        List<EncryptedFileName> result = new ArrayList<>();
        for (EncryptedFileName ep : epList) {
            if (ep.getReceiverId().equals(receiverId)) {
                result.add(ep);
            }
        }

        return result;
    }

    public int findSizeEncryptedFileList() {
        return sampleData.getEfList().size();
    }   // 모든 EncryptedFile 반환
}
